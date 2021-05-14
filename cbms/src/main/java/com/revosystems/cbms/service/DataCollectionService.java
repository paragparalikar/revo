package com.revosystems.cbms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.domain.model.Sensor;
import com.revosystems.cbms.util.Ports;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataCollectionService implements Runnable, SerialPortDataListener {
	private static final byte[] REQUEST = new byte[]{1,3,0,0,0,8,68,12};
	
	@Getter @Setter
	private long delayMillis;
	
	private volatile long lastRequestTimestamp = 0;
	
	private SerialPort port;
	
	@Value("${port.name}")
	private String portName;
	
	@NonNull 
	private final MetricService metricService;
	
	@NonNull
	private final ChannelConfigurationService channelConfigurationService;
	
	@Override
	@Scheduled(fixedDelay = 1000)
	public void run() {
		final boolean enabled = 0 < delayMillis;
		if(enabled && (null == port || !port.isOpen() || !Ports.hasName(portName, port))) {
			if(null == port) log.info("Looking up and opening port for first time");
			else if(!port.isOpen()) log.info("Port is not open, looking up and opening it again");
			else if(!Ports.hasName(portName, port)) log.info("Port name has changed, looking up and opening appropriate port");
			
			if(null != port) port.removeDataListener();
			port = Ports.findByName(portName);
			if(null != port) {
				port.setBaudRate(19200);
				port.setNumDataBits(8);
				port.setNumStopBits(1);
				port.setParity(SerialPort.NO_PARITY);
				port.openPort();
				port.addDataListener(this);
				log.info("Started listening on port {}", Ports.toString(port));
			}
		}
		
		if(!enabled && null != port && port.isOpen()) {
			log.info("Stopping listenint on port {}", port.getDescriptivePortName());
			port.removeDataListener();
			port.closePort();
			port = null;
		}
		
		final long now = System.currentTimeMillis();
		if(enabled && null != port && port.isOpen() && lastRequestTimestamp + delayMillis <= now) {
			log.info("Sending request to port {}", port.getDescriptivePortName());
			port.writeBytes(REQUEST, 8);
			while(port.bytesAwaitingWrite() > 0) {}
			lastRequestTimestamp = now;
			log.info("Request sent successfully to port {}", port.getDescriptivePortName());
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {
		log.info("Received data from port");
		final byte[] response = event.getReceivedData();
		for(int valueIndex = 3, channelIndex = 0; valueIndex < 19; valueIndex += 2, channelIndex ++) {
			final int value = response[valueIndex] << 8 & 0xFF00 | response[valueIndex + 1] & 0xFF;
			final Channel channel = Channel.values()[channelIndex];
			final Metric metric = toMetric(channel, value);
			if(null != metric) {
				metricService.save(metric);
			}
		}
	}
	
	private Metric toMetric(final Channel channel, final double value) {
		if(null == channel) return null;
		final long timestamp = System.currentTimeMillis();
		return Optional.ofNullable(value)
				.map(l -> Optional.ofNullable(channel)
						.flatMap(channelConfigurationService::findById)
						.map(channelConfiguration -> Metric.build(channelConfiguration, timestamp, rationalize(value, channelConfiguration.getSensor())))
						.orElse(null))
				.orElse(null);
	}
	
	private double rationalize(final double value, final Sensor sensor) {
		if(sensor.getMax() == sensor.getMin()) return value;
		return (value - 4) * (sensor.getMax() - sensor.getMin()) / 16;
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

}
