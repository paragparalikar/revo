package com.revo.oms.integration;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.util.StringUtils;

import com.revo.oms.model.Kit;
import com.revo.oms.model.Part;
import com.revo.oms.model.Requisition;
import com.revo.oms.service.KitService;
import com.revo.oms.service.PartService;

import lombok.NonNull;

@Configuration
public class MqttConfiguration {
	
	@Autowired private KitService kitService;
	@Autowired private PartService partService;
	
	@Bean
	@ConfigurationProperties("mqtt")
	public MqttConnectOptions mqttConnectOptions() {
		return new MqttConnectOptions();
	}

	@Bean
	public MqttPahoMessageDrivenChannelAdapter mqttMessageProducer(
			@NonNull final MqttConnectOptions options,
			@NonNull @Value("${mqtt.client-id}") final String clientId,
			@NonNull @Value("${mqtt.topic}") final String topic) {
		final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setConnectionOptions(options);
		return new MqttPahoMessageDrivenChannelAdapter(clientId, factory, topic);
	}
	
	private Requisition transform(final String payload) {
		if(StringUtils.hasText(payload)) {
			final String[] tokens = payload.split(",");
			final Kit kit = kitService.getOne(Long.parseLong(tokens[0]));
			final Part part = partService.getOne(Long.parseLong(tokens[1]));
			final Long quantity = Long.parseLong(tokens[2]);
			final Requisition requisition = new Requisition();
			requisition.setKit(kit);
			requisition.setPart(part);
			requisition.setQuantity(quantity);
			return requisition;
		}
		return null;
	}
	
	
	@Bean
	public IntegrationFlow mqttRequisitionFlow(@NonNull final MqttPahoMessageDrivenChannelAdapter mqttMessageSource) {
		return IntegrationFlows
				.from(mqttMessageSource)
				.transform(this::transform)
				.channel("requisitionChannel")
				.get();
	}

}
