package com.revo.oms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.revo.oms.model.Part;

@Repository
@RestResource
public interface PartRepository extends JpaRepository<Part, Long> {

}
