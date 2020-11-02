package com.revo.rms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revo.rms.model.Requisition;
import com.revo.rms.model.RequisitionStatus;

@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, Long> {

	public List<Requisition> findByStatus(RequisitionStatus status);
	
	public Integer countByStatus(RequisitionStatus status);
	
}
