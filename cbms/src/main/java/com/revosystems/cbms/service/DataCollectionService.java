package com.revosystems.cbms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListenerWithExceptions;
import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.domain.model.Sensor;
import com.revosystems.cbms.util.Ports;
import com.revosystems.cbms.util.Strings;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataCollectionService implements Runnable, SerialPortMessageListenerWithExceptions {
	private static final byte[] REQUEST = "010300000008440C".getBytes(); 
	
	@Getter @Setter
	private long delayMillis;
	
	private long lastRequestTimestamp = 0;
	
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
				port.openPort();
				port.addDataListener(this);
				log.info("Started listening on port {}", Ports.toString(port));
			}
		}
		
		if(!enabled && null != port && port.isOpen()) {
			port.removeDataListener();
			port.closePort();
			port = null;
		}
		
		final long now = System.currentTimeMillis();
		if(enabled && null != port && port.isOpen() && lastRequestTimestamp + now > delayMillis) {
			port.writeBytes(REQUEST, REQUEST.length);
			log.debug("Sending request to port {}", port.getDescriptivePortName());
			lastRequestTimestamp = now;
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {
		final String response = new String(event.getReceivedData());
		for(int valueIndex = 6, channelIndex = 0; valueIndex < 38; valueIndex += 4, channelIndex ++) {
			final String line = response.substring(valueIndex, valueIndex + 4);
			final Channel channel = Channel.values()[channelIndex];
			final Metric metric = toMetric(channel, line);
			if(null != metric) {
				metricService.save(metric);
			}
		}
	}
	
	private Metric toMetric(final Channel channel, final String value) {
		if(null == channel || Strings.isBlank(value)) return null;
		final long timestamp = System.currentTimeMillis();
		final double doubleValue = Double.parseDouble(value);
		
		return Optional.ofNullable(value)
				.filter(Strings::isNotBlank)
				.map(l -> Optional.ofNullable(channel)
						.flatMap(channelConfigurationService::findById)
						.map(channelConfiguration -> Metric.build(channelConfiguration, timestamp, rationalize(doubleValue, channelConfiguration.getSensor())))
						.orElse(null))
				.orElse(null);
	}
	
	private double rationalize(final double value, final Sensor sensor) {
		if(sensor.getMax() == sensor.getMin()) return value;
		return (value - 4) * (sensor.getMax() - sensor.getMin()) / 16;
	}
	
	@Override
	public void catchException(Exception e) {
		log.error(e.getMessage(),e);
	}
	
	@Override
	public boolean delimiterIndicatesEndOfMessage() {
		return true;
	}
	
	@Override
	public byte[] getMessageDelimiter() {
		return "\r\n".getBytes();
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

}
