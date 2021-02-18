package com.revosystems.cbms;

import java.time.Duration;
import java.util.Calendar;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.domain.model.Metric;
import com.revosystems.cbms.domain.model.Sensor;
import com.revosystems.cbms.domain.model.Thing;
import com.revosystems.cbms.repository.ChannelConfigurationRepository;
import com.revosystems.cbms.repository.MetricRepository;
import com.revosystems.cbms.repository.SensorRepository;
import com.revosystems.cbms.repository.ThingRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DevDataSetup implements CommandLineRunner {

	private final ThingRepository thingRepository;
	private final SensorRepository sensorRepository;
	private final MetricRepository metricRepository;
	private final ChannelConfigurationRepository channelConfigurationRepository;
	
	@Override
	public void run(String... args) throws Exception {
		if(0 == thingRepository.count()) {
			things().forEach(thingRepository::save);
			sensors().forEach(sensorRepository::save);
			
			final Iterable<Thing> things = thingRepository.findAll();
			final Iterable<Sensor> sensors = sensorRepository.findAll();
			
			createMetrics(things, sensors);
			createChannelConfigurations(things, sensors);
		}
	}
	
	private void createChannelConfigurations(Iterable<Thing> things, Iterable<Sensor> sensors) {
		final Map<Channel, ChannelConfiguration> configs = new EnumMap<>(Channel.class);
		for(Thing thing : things) {
			for(Sensor sensor : sensors) {
				for(Channel channel : Channel.values()) {
					configs.put(channel, new ChannelConfiguration(channel, thing, sensor));
				}
			}
		}
		configs.values().forEach(channelConfigurationRepository::save);
	}
	
	private void createMetrics(Iterable<Thing> things, Iterable<Sensor> sensors) {
		for(Thing thing : things) {
			for(Sensor sensor : sensors) {
				final Calendar calendar = Calendar.getInstance();
				for(int index = -1; index > -4; index--) {
					calendar.add(Calendar.MONTH, index);
					final long from = calendar.getTimeInMillis();
					calendar.add(Calendar.DATE, 4);
					final long to = calendar.getTimeInMillis();
					final long increment = Duration.ofMinutes(15).toMillis();
					metrics(thing, sensor, from, to, increment)
						.forEach(metricRepository::save);
				}
			}
		}
	}
	
	private List<Metric> metrics(Thing thing, Sensor sensor, long from, long to, long increment){
		final Random random = new Random();
		final double range = sensor.getMax() - sensor.getMin();
		final List<Metric> metrics = new LinkedList<>();
		for(long timestamp = from; timestamp < to; timestamp += increment) {
			final double value = sensor.getMin() + (random.nextDouble() * range);
			metrics.add(new Metric(thing.getId(), sensor.getId(), value, timestamp));
		}
		return metrics;
	}
	
	private Collection<Thing> things() {
		final List<Thing> things = new LinkedList<>();
		for(int index = 1; index <= 20; index ++) {
			final Thing thing = new Thing();
			thing.setName("Machine-"+index);
			things.add(thing);
		}
		return things;
	}
	
	private Collection<Sensor> sensors(){
		final List<Sensor> sensors = new LinkedList<>();
		sensors.add(new Sensor(null, "Temparature", "celcius", 0d, 100d));
		sensors.add(new Sensor(null, "Vibration", "mm/s", 0d, 100d));
		sensors.add(new Sensor(null, "pressure", "bar", 0d, 100d));
		return sensors;
	}

}
