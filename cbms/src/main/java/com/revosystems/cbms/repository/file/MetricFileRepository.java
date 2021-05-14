package com.revosystems.cbms.repository.file;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;

import org.springframework.util.FileSystemUtils;

import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.repository.MetricRepository;
import com.revosystems.cbms.repository.file.mapper.MetricMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	public synchronized void purgeAll() throws IOException {
		FileSystemUtils.deleteRecursively(path);
		init();
	}
	
	@Override
	public synchronized void exportAll(OutputStream outputStream) throws IOException {
		final Path path = this.path.getParent();
		final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				final String name = path.relativize(dir).toString() + "/";
				final ZipEntry zipEntry = new ZipEntry(name);
				zipOutputStream.putNextEntry(zipEntry);
				zipOutputStream.closeEntry();
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				final String name = path.relativize(file).toString();
				final ZipEntry zipEntry = new ZipEntry(name);
				zipOutputStream.putNextEntry(zipEntry);
				Files.copy(file, zipOutputStream);
				zipOutputStream.closeEntry();
				return FileVisitResult.CONTINUE;
			}
		});
		zipOutputStream.close();
	}
	
	@Override
	@SneakyThrows
	public synchronized Metric save(Metric metric) {
		final Path path = resolve(metric.getThingId(), metric.getSensorId());
		Files.write(path, mapper.map(metric), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
		log.info(metric.toString());
		return metric;
	}

	@Override
	@SneakyThrows
	public synchronized List<Metric> query(Long thingId, Long sensorId, Long from, Long to) {
		try(final RandomAccessFile file = new RandomAccessFile(resolve(thingId, sensorId).toFile(), "r")){
			if(from > mapper.lastTimestamp(file)) return Collections.emptyList();
			if(to < mapper.firstTimestamp(file)) return Collections.emptyList();
			final List<Metric> metrics = new ArrayList<>();
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
