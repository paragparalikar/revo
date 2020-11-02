package com.revo.rms.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.revo.rms.model.Part;
import com.revo.rms.model.dto.PartDTO;
import com.revo.rms.service.KitService;

public abstract class PartMapperDecorator implements PartMapper {

	@Autowired
	@Qualifier("delegate")
	private PartMapper delegate;
	
	@Autowired
	private KitService kitService;

	@Override
	public Part fromDTO(PartDTO dto) {
		final Part part = delegate.fromDTO(dto);
		part.setKit(kitService.getOne(dto.getKitId()));
		return part;
	}
	
}
