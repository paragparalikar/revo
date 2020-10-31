package com.revo.oms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.revo.oms.model.Kit;

@Repository
@RestResource
public interface KitRepository extends JpaRepository<Kit, Long> {

}
