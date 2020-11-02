package com.revo.rms.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.revo.rms.model.Requisition;
import com.revo.rms.model.dto.RequisitionDTO;
import com.revo.rms.service.PartService;
import com.revo.rms.service.StationService;

public abstract class RequisitionMapperDecorator implements RequisitionMapper{

	@Autowired
	@Qualifier("delegate")
	private RequisitionMapper delegate;
	
	@Autowired
	private PartService partService;
	
	@Autowired
	private StationService stationService;
	
	@Override
	public Requisition fromDTO(RequisitionDTO dto) {
		final Requisition requisition = delegate.fromDTO(dto);
		requisition.setPart(partService.getOne(dto.getPartId()));
		requisition.setStation(stationService.getOne(dto.getStationId()));
		return requisition;
	}
	
}
