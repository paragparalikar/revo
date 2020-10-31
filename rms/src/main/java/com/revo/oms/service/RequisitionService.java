package com.revo.oms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revo.oms.model.Requisition;
import com.revo.oms.model.RequisitionStatus;
import com.revo.oms.repository.RequisitionRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequisitionService {

	private final RequisitionRepository requisitionRepository;
	
	public Requisition save(@NonNull final Requisition requisition) {
		return requisitionRepository.saveAndFlush(requisition);
	}
	
	public List<Requisition> findByStatus(@NonNull final RequisitionStatus status){
		return requisitionRepository.findByStatus(status);
	}
	
	public Integer countByStatus(@NonNull final RequisitionStatus status) {
		return requisitionRepository.countByStatus(status);
	}
	
}
