package com.revosystems.cbms.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.repository.MetricRepository;
import com.revosystems.cbms.util.Strings;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class MetricService implements SerialPortDataListener {

	@NonNull
	@Delegate
	private final MetricRepository metricRepository;
	
	@NonNull
	private final ChannelConfigurationService channelConfigurationService;

	@Getter
	private boolean running;

	@PostConstruct
	public void start() {
		final SerialPort comPort = SerialPort.getCommPorts()[0];
		comPort.openPort();
		comPort.addDataListener(this);
		System.out.println(String.join(" - ", comPort.getSystemPortName(), comPort.getDescriptivePortName(), comPort.getPortDescription()));
	}

	@Override
	public int getListeningEvents() {
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		final List<String> lines = Strings.toLines(event.getReceivedData());
		if(null == lines) throw new IllegalArgumentException("Values can not be null");
		if(lines.isEmpty()) throw new IllegalArgumentException("Values can not be empty");
		while(Channel.values().length > lines.size()) lines.add(Strings.EMPTY);
		lines.stream()
			.map(this::toMetrics)
			.flatMap(Collection::stream)
			.forEach(metricRepository::save);
	}
	
	private List<Metric> toMetrics(final String line){
		final List<Metric> metrics = new ArrayList<>();
		final String[] values = line.split(",");
		for(int index = 0; index < Channel.values().length; index++) {
			final String value = values[index];
			final Channel channel = Channel.values()[index];
			metrics.add(toMetric(channel, value));
		}
		return metrics;
	}
	
	private Metric toMetric(final Channel channel, final String value) {
		return Optional.ofNullable(value)
				.filter(Strings::isNotBlank)
				.map(l -> Optional.ofNullable(channel)
						.flatMap(channelConfigurationService::findById)
						.map(channelConfiguration -> toMetric(channelConfiguration, value))
						.orElse(null))
				.orElse(null);
	}

	private Metric toMetric(final ChannelConfiguration channelConfiguration, final String value) {
		final Metric metric = new Metric();
		metric.setValue(Double.parseDouble(value));
		metric.setTimestamp(System.currentTimeMillis());
		metric.setSensorId(channelConfiguration.getSensor().getId());
		metric.setThingId(channelConfiguration.getThing().getId());
		return metric;
	}
}
