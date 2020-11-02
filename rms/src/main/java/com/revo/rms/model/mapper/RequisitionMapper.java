package com.revo.rms.model.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.revo.rms.model.Requisition;
import com.revo.rms.model.dto.RequisitionDTO;

@Mapper(componentModel = "spring")
@DecoratedWith(RequisitionMapperDecorator.class)
public interface RequisitionMapper {

	@Mapping(target = "part", ignore = true)
	Requisition fromDTO(RequisitionDTO dto);
	
	@Mapping(target = "partId", source = "part.id")
	RequisitionDTO toDTO(Requisition requisition);
		
}
