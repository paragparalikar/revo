package com.revosystems.cbms.service;

import org.springframework.stereotype.Service;

import com.revosystems.cbms.repository.ChannelConfigurationRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class ChannelConfigurationService {

	@NonNull
	@Delegate
	private final ChannelConfigurationRepository channelConfigurationRepository;
	
}
