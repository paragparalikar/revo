package com.revo.llms.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
	
}
