package com.revo.rms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revo.rms.model.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

}
