package com.revosystems.cbms.repository.file.mapper;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import com.revosystems.cbms.domain.model.Metric;

import lombok.SneakyThrows;

public class MetricMapper {
	private static final int BYTES = Long.BYTES + Double.BYTES;
	
	@SneakyThrows
	public long firstTimestamp(RandomAccessFile file) {
		if(file.length() < BYTES) return 0;
		file.seek(0);
		return file.readLong();
	}
	
	@SneakyThrows
	public long lastTimestamp(RandomAccessFile file) {
		if(file.length() < BYTES) return 0;
		file.seek(file.length() - BYTES);
		return file.readLong();
	}
	
	@SneakyThrows
	public boolean canRead(RandomAccessFile file) {
		return file.length() >= BYTES;
	}

	public byte[] map(Metric metric) {
	    final ByteBuffer buffer = ByteBuffer.allocate(BYTES);
	    buffer.putLong(metric.getTimestamp());
	    buffer.putDouble(metric.getValue());
	    return buffer.array();
	}
	
	@SneakyThrows
	public Metric map(RandomAccessFile file) {
		final Metric metric = new Metric();
		metric.setTimestamp(file.readLong());
		metric.setValue(file.readDouble());
		return metric;
	}
	
}
