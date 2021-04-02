package com.revosystems.cbms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListenerWithExceptions;
import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.util.Ports;
import com.revosystems.cbms.util.Strings;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
public class DataCollectionService implements Runnable, SerialPortMessageListenerWithExceptions {
	
	@Getter @Setter
	private boolean enabled = false;
	
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
		if(enabled && (null == port || !port.isOpen() || !Ports.hasName(portName, port))) {
			if(null != port) port.removeDataListener();
			port = Ports.findByName(portName);
			if(null != port) {
				port.openPort();
				port.addDataListener(this);
				System.out.println("Started listening on port " + Ports.toString(port));
			}
		}
		
		if(!enabled && null != port && port.isOpen()) {
			port.removeDataListener();
			port.closePort();
			port = null;
		}
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) {
		final List<String> lines = Strings.toLines(event.getReceivedData());
		if(null == lines) throw new IllegalArgumentException("Values can not be null");
		if(lines.isEmpty()) throw new IllegalArgumentException("Values can not be empty");
		lines.stream()
			.map(this::toMetrics)
			.flatMap(Collection::stream)
			.filter(Objects::nonNull)
			.forEach(metricService::save);
	}
	
	private List<Metric> toMetrics(final String line){
		final List<Metric> metrics = new ArrayList<>();
		final String[] values = Arrays.copyOf(line.split(","), Channel.values().length);
		for(int index = 0; index < Channel.values().length; index++) {
			final String value = values[index];
			final Channel channel = Channel.values()[index];
			metrics.add(toMetric(channel, value));
		}
		return metrics;
	}
	
	private Metric toMetric(final Channel channel, final String value) {
		if(null == channel || Strings.isBlank(value)) return null;
		final long timestamp = System.currentTimeMillis();
		final double doubleValue = Double.parseDouble(value);
		return Optional.ofNullable(value)
				.filter(Strings::isNotBlank)
				.map(l -> Optional.ofNullable(channel)
						.flatMap(channelConfigurationService::findById)
						.map(channelConfiguration -> Metric.build(channelConfiguration, timestamp, doubleValue))
						.orElse(null))
				.orElse(null);
	}
	
	@Override
	public void catchException(Exception e) {
		e.printStackTrace();
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
