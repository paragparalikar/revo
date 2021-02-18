package com.revosystems.cbms.repository.file;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

import com.revosystems.cbms.domain.intf.Identifiable;
import com.revosystems.cbms.repository.file.mapper.StringMapper;

import lombok.NonNull;

public class GeneratedIdFileRepository<T extends Identifiable<Long>> extends FileRepository<T, Long> {

	private final AtomicLong idProvider = new AtomicLong(0l);
	
	public GeneratedIdFileRepository(@NonNull Path path, @NonNull StringMapper<T> mapper) {
		super(path, mapper);
	}
	
	@Override
	protected void cache(T entity) {
		super.cache(entity);
		idProvider.set(Math.max(idProvider.get(), entity.getId()));
	}

	@Override
	public <S extends T> S save(S entity) {
		if(null == entity.getId() || 0 >= entity.getId()) entity.setId(idProvider.incrementAndGet());
		return super.save(entity);
	}
	
}
