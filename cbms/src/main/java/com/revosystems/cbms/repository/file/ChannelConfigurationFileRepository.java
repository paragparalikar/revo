package com.revosystems.cbms.repository.file;

import java.nio.file.Path;

import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.repository.ChannelConfigurationRepository;
import com.revosystems.cbms.repository.file.mapper.StringMapper;

import lombok.NonNull;

public class ChannelConfigurationFileRepository extends FileRepository<ChannelConfiguration, Channel>
		implements ChannelConfigurationRepository {

	public ChannelConfigurationFileRepository(@NonNull Path path, @NonNull StringMapper<ChannelConfiguration> mapper) {
		super(path, mapper);
	}

}
