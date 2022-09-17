package com.revo.llms.station;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {

	List<Station> findAllByOrderByIdAsc();
	
	boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);
	
}
