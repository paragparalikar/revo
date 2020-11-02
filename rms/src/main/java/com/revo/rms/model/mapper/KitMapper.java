package com.revo.rms.model.mapper;

import org.mapstruct.Mapper;

import com.revo.rms.model.Kit;
import com.revo.rms.model.dto.KitDTO;

@Mapper(componentModel = "spring")
public interface KitMapper {

	Kit map(KitDTO dto);
	
	KitDTO map(Kit kit);
	
}
