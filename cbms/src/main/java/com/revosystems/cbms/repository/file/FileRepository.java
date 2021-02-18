package com.revosystems.cbms.repository.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.revosystems.cbms.domain.intf.Identifiable;
import com.revosystems.cbms.repository.file.mapper.StringMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class FileRepository<T extends Identifiable<ID>, ID> implements Repository<T, ID>{
	
	@NonNull private final Path path;
	@NonNull private final StringMapper<T> mapper;
	@Getter(AccessLevel.PROTECTED) private final Map<ID, T> cache = new HashMap<>();
	
	
	protected void init() {
		readAll();
	}

	protected void cache(T entity) {
		cache.put(entity.getId(), entity);
	}
	
	@SneakyThrows
	protected void readAll() {
		if(cache.isEmpty() && Files.exists(path)) {
			Files.lines(path).map(mapper::map).forEach(this::cache);
		}
	}
	
	@SneakyThrows
	protected void writeAll() {
		if(!cache.isEmpty()) {
			if(!Files.exists(path)) Files.createDirectories(path.getParent());
			final List<String> lines = cache.values().stream().map(mapper::map).collect(Collectors.toList());
			Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		}
	}
	
	@Override
	public <S extends T> S save(S entity) {
		init();
		cache.put(entity.getId(), entity);
		writeAll();
		return entity;
	}
	
	@Override
	public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
		init();
		entities.forEach(entity -> cache.put(entity.getId(), entity));
		writeAll();
		return entities;
	}
	
	@Override
	public Optional<T> findById(ID id) {
		init();
		return Optional.ofNullable(cache.get(id));
	}
	
	@Override
	public boolean existsById(ID id) {
		init();
		return cache.containsKey(id);
	}
	
	@Override
	public Iterable<T> findAll() {
		init();
		return cache.values();
	}
	
	@Override
	public Iterable<T> findAllById(Iterable<ID> ids) {
		init();
		return StreamSupport.stream(ids.spliterator(), false)
				.map(cache::get)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	
	@Override
	public long count() {
		init();
		return cache.size();
	}
	
	@Override
	public void deleteById(ID id) {
		init();
		if(null != cache.remove(id)) {
			writeAll();
		}
	}
	
	@Override
	public void delete(T entity) {
		init();
		if(null != cache.remove(entity.getId())) {
			writeAll();
		}
	}
	
	@Override
	public void deleteAll(Iterable<? extends T> entities) {
		init();
		if(0 < StreamSupport.stream(entities.spliterator(), false)
			.map(Identifiable::getId)
			.map(cache::remove)
			.filter(Objects::nonNull)
			.count()) {
			writeAll();
		}
			
	}
	
	@Override
	@SneakyThrows
	public void deleteAll() {
		init();
		cache.clear();
		Files.deleteIfExists(path);
	}
	
}

