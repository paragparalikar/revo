package com.revo.llms.reason;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revo.llms.category.Category;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Long> {

	List<Reason> findByCategory(Category category);
	
	boolean existsByTextIgnoreCase(String text);
	
}
