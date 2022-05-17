package com.revo.llms.part;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
	
	boolean existsByNameAndProductId(String name, Long productId);
	
	Long countByProductId(Long productId);
	
	void deleteByProductId(Long productId);
	
	List<Part> findByProductId(Long productId);

	List<Part> findByProductId(Long productId, Pageable pageable);
	
}
