package com.revosystems.cbms.repository.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.repository.MetricRepository;
import com.revosystems.cbms.repository.file.mapper.MetricMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class MetricFileRepository implements MetricRepository {

	@NonNull private final Path path;
	@NonNull private final MetricMapper mapper;
	
	@PostConstruct
	public void init() throws IOException {
		Files.createDirectories(path);
	}
	
	private Path resolve(Long thingId, Long sensorId) {
		return path.resolve(String.join(".", String.valueOf(thingId), String.valueOf(sensorId), "dat"));
	}
	
	@Override
	@SneakyThrows
	public Metric save(Metric metric) {
		final Path path = resolve(metric.getThingId(), metric.getSensorId());
		Files.write(path, mapper.map(metric), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
		return metric;
	}

	@Override
	@SneakyThrows
	public List<Metric> query(Long thingId, Long sensorId, Long from, Long to) {
		try(final RandomAccessFile file = new RandomAccessFile(resolve(thingId, sensorId).toFile(), "r")){
			if(from > mapper.lastTimestamp(file)) return Collections.emptyList();
			if(to < mapper.firstTimestamp(file)) return Collections.emptyList();
			final List<Metric> metrics = new LinkedList<>();
			file.seek(0);
			while(mapper.canRead(file)) {
				final Metric metric = mapper.map(file);
				if(from <= metric.getTimestamp() && to >= metric.getTimestamp()) {
					metric.setThingId(thingId);
					metric.setSensorId(sensorId);
					metrics.add(metric);
				}
			}
			return metrics;
		}
	}

}
