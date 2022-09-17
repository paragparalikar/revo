package com.revo.llms.category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
	public Category save(Category category) {
		return categoryRepository.save(category);
	}
	
	public long count() {
		return categoryRepository.count();
	}
	
	public List<Category> findAll(){
		return categoryRepository.findAll();
	}
	
	public Page<Category> findAll(Pageable pageable){
		return categoryRepository.findAll(pageable);
	}
	
	public boolean existsByNameIgnoreCaseAndIdNot(String name, Long id) {
		return categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id);
	}
	
	public void delete(Category category) {
		categoryRepository.delete(category);
	}

}
