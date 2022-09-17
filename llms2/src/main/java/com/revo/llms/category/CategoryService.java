package com.revo.llms.category;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
	public Category save(Category category) {
		return categoryRepository.save(category);
	}
	
	public List<Category> findAll(){
		return categoryRepository.findAll();
	}
	
	public boolean existsByNameIgnoreCaseAndIdNot(String name, Long id) {
		return categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id);
	}
	
	public void delete(Category category) {
		categoryRepository.delete(category);
	}

}
