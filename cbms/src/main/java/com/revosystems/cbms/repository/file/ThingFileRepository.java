package com.revosystems.cbms.repository.file;

import java.nio.file.Path;

import com.revosystems.cbms.domain.model.Thing;
import com.revosystems.cbms.repository.ThingRepository;
import com.revosystems.cbms.repository.file.mapper.StringMapper;

import lombok.NonNull;

public class ThingFileRepository extends GeneratedIdFileRepository<Thing> implements ThingRepository {

	public ThingFileRepository(@NonNull Path path, @NonNull StringMapper<Thing> mapper) {
		super(path, mapper);
	}

}
