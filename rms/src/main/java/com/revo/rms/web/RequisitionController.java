package com.revo.rms.web;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revo.rms.model.Requisition;
import com.revo.rms.model.RequisitionStatus;
import com.revo.rms.model.dto.RequisitionDTO;
import com.revo.rms.model.mapper.RequisitionMapper;
import com.revo.rms.service.RequisitionService;
import com.revo.rms.web.ui.RequisitionView;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requisitions")
public class RequisitionController {

	private final RequisitionMapper requisitionMapper;
	private final RequisitionService requisitionService;
	
	@GetMapping
	public List<RequisitionDTO> findByStatus(@RequestParam final RequisitionStatus status) {
		final List<Requisition> requisitions = requisitionService.findByStatus(status);
		return requisitions.stream().map(requisitionMapper::toDTO).collect(Collectors.toList());
	}
	
	@PostMapping
	public Long create(@Valid @NonNull @RequestBody final RequisitionDTO dto) {
		validateCreate(dto);
		final Requisition requisition = requisitionMapper.fromDTO(dto);
		requisition.setStatus(RequisitionStatus.OPEN);
		final Requisition entity = requisitionService.save(requisition);
		RequisitionView.refreshAll(entity);
		return entity.getId();
	}
	
	private void validateCreate(final RequisitionDTO dto) {
		Objects.requireNonNull(dto.getPartId());
		Objects.requireNonNull(dto.getStationId());
		if(Objects.nonNull(dto.getId())) throw new IllegalArgumentException("id must be null");
		if(Objects.nonNull(dto.getStatus())) throw new IllegalArgumentException("status must be null");
	}
	
	@PutMapping
	public void update(@Valid @NonNull @RequestBody final RequisitionDTO dto) {
		validateUpdate(dto);
		final Requisition requisition = requisitionService.getOne(dto.getId());
		requisition.setStatus(dto.getStatus());
		requisitionService.save(requisition);
		RequisitionView.refreshAll(requisition);
	}
	
	private void validateUpdate(final RequisitionDTO dto) {
		Objects.requireNonNull(dto.getId());
		Objects.requireNonNull(dto.getStatus());
	} 
	
}
