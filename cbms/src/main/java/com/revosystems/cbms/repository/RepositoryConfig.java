package com.revosystems.cbms.repository;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.domain.model.Sensor;
import com.revosystems.cbms.domain.model.Thing;
import com.revosystems.cbms.repository.file.ChannelConfigurationFileRepository;
import com.revosystems.cbms.repository.file.MetricFileRepository;
import com.revosystems.cbms.repository.file.SensorFileRepository;
import com.revosystems.cbms.repository.file.ThingFileRepository;
import com.revosystems.cbms.repository.file.mapper.ChannelConfigurationCsvMapper;
import com.revosystems.cbms.repository.file.mapper.MetricMapper;
import com.revosystems.cbms.repository.file.mapper.SensorCsvMapper;
import com.revosystems.cbms.repository.file.mapper.StringMapper;
import com.revosystems.cbms.repository.file.mapper.ThingCsvMapper;


@Configuration
public class RepositoryConfig {

	@Value("${user.home}")
	private Path home;
	
	private Path path(String fileName) {
		return home.resolve(Paths.get("cbms", fileName));
	}
	
	@Bean
	public MetricRepository metricRepository() {
		return new MetricFileRepository(path("metrics"), new MetricMapper());
	}
	
	@Bean
	public ChannelConfigurationRepository channelConfigurationRepository(
			ThingRepository thingRepository,
			SensorRepository sensorRepository){
		final StringMapper<ChannelConfiguration> mapper = 
				new ChannelConfigurationCsvMapper(thingRepository, sensorRepository);
		return new ChannelConfigurationFileRepository(path("channel-configurations.csv"), mapper);
	}
	
	@Bean
	public ThingRepository thingRepository() {
		final StringMapper<Thing> mapper = new ThingCsvMapper();
		return new ThingFileRepository(path("things.csv"), mapper);
	}
	
	@Bean
	public SensorRepository sensorRepository() {
		final StringMapper<Sensor> mapper = new SensorCsvMapper();
		return new SensorFileRepository(path("sensors.csv"), mapper);
	}
	
	
}
