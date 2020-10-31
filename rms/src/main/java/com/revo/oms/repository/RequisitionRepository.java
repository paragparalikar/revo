package com.revo.oms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.revo.oms.model.Requisition;
import com.revo.oms.model.RequisitionStatus;

@Repository
@RestResource
public interface RequisitionRepository extends JpaRepository<Requisition, Long> {

	public List<Requisition> findByStatus(RequisitionStatus status);
	
	public Integer countByStatus(RequisitionStatus status);
	
}
