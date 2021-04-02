package com.revosystems.cbms.web.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.util.comparator.Comparators;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.model.ChannelConfiguration;
import com.revosystems.cbms.service.ChannelConfigurationService;
import com.revosystems.cbms.web.dto.ChannelConfigurationDto;
import com.revosystems.cbms.web.mapper.ChannelConfigurationMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/channel-configurations")
public class ChannelConfigurationController {
	
	@NonNull private final ChannelConfigurationMapper cahnnelConfigurationMapper;
	@NonNull private final ChannelConfigurationService channelConfigurationService;
	private final Comparator<ChannelConfigurationDto> comparator = Comparators
			.<ChannelConfigurationDto>nullsLow()
			.thenComparing(ChannelConfigurationDto::getChannel);
	
	@PatchMapping
	@ResponseBody
	public ChannelConfigurationDto save(@RequestBody final ChannelConfigurationDto dto) {
		final ChannelConfiguration channelConfiguration = cahnnelConfigurationMapper.map(dto);
		final ChannelConfiguration result = channelConfigurationService.save(channelConfiguration);
		return cahnnelConfigurationMapper.map(result);
	}

	@PostMapping
	@ResponseBody
	public List<ChannelConfigurationDto> saveAll(@RequestBody @NotEmpty final List<@Valid ChannelConfigurationDto> dtos){
		final List<ChannelConfiguration> channelConfigurations = dtos.stream().map(cahnnelConfigurationMapper::map).collect(Collectors.toList());
		final Iterable<ChannelConfiguration> iterable = channelConfigurationService.saveAll(channelConfigurations);
		return StreamSupport.stream(iterable.spliterator(), false)
				.map(cahnnelConfigurationMapper::map)
				.sorted(comparator)
				.collect(Collectors.toList());
	}
	
	@GetMapping
	@ResponseBody
	public List<ChannelConfigurationDto> findAll(){
		final List<ChannelConfigurationDto> dtos = StreamSupport.stream(channelConfigurationService.findAll().spliterator(), false)
				.map(cahnnelConfigurationMapper::map)
				.collect(Collectors.toList());
		Arrays.asList(Channel.values()).forEach(channel -> {
			if(!dtos.stream().anyMatch(dto -> channel.equals(dto.getChannel()))) {
				dtos.add(new ChannelConfigurationDto(channel, null, null));
			}
		});
		dtos.sort(comparator);
		return dtos;
	}
	
}
