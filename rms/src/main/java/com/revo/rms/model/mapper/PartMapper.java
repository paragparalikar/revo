package com.revo.rms.model.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.revo.rms.model.Part;
import com.revo.rms.model.dto.PartDTO;

@Mapper(componentModel = "spring")
@DecoratedWith(PartMapperDecorator.class)
public interface PartMapper {

	@Mapping(target = "kit", ignore = true)
	Part fromDTO(PartDTO dto);
	
	@Mapping(target = "kitId", source = "kit.id")
	PartDTO toDTO(Part part);
	
}
