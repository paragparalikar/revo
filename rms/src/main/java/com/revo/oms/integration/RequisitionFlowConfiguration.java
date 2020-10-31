package com.revo.oms.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

import com.revo.oms.model.Requisition;
import com.revo.oms.service.RequisitionService;
import com.revo.oms.ui.RequisitionView;

import lombok.NonNull;

@Configuration
public class RequisitionFlowConfiguration {
	
	@Autowired
	private RequisitionService requisitionService;
	
	@Bean
	public MessageChannel requisitionChannel() {
		return MessageChannels.direct().get();
	}
	
	@Bean
	public IntegrationFlow flow() {
		return IntegrationFlows.from(requisitionChannel())
				.handle(requisitionService, "save")
				.handle(message -> RequisitionView.refreshAll((@NonNull Requisition) message.getPayload()))
				.get();
	}
}
