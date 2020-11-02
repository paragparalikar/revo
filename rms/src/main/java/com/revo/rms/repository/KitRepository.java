package com.revo.rms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revo.rms.model.Kit;

@Repository
public interface KitRepository extends JpaRepository<Kit, Long> {

}
