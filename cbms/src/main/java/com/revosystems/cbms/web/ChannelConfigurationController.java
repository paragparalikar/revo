package com.revosystems.cbms.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.service.ChannelConfigurationService;
import com.revosystems.cbms.web.dto.ChannelConfigurationDto;
import com.revosystems.cbms.web.mapper.ChannelConfigurationMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel-configurations")
public class ChannelConfigurationController {
	
	@NonNull private final ChannelConfigurationMapper cahnnelConfigurationMapper;
	@NonNull private final ChannelConfigurationService channelConfigurationService;

	@PostMapping
	@ResponseBody
	public List<ChannelConfigurationDto> create(@RequestBody @NotEmpty final List<@Valid ChannelConfiguration> channelConfigurations){
		return channelConfigurations.stream()
			.map(channelConfigurationService::save)
			.map(cahnnelConfigurationMapper::map)
			.collect(Collectors.toList());
	}
	
	@GetMapping
	@ResponseBody
	public List<ChannelConfigurationDto> findAll(){
		return StreamSupport.stream(channelConfigurationService.findAll().spliterator(), false)
				.map(cahnnelConfigurationMapper::map)
				.collect(Collectors.toList());
	}
	
}
