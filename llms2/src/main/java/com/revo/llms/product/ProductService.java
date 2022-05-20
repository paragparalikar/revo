package com.revo.llms.product;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.revo.llms.part.PartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	
	private final PartService partService;
	
	public long count() {
		return productRepository.count();
	}
	
	public Product save(Product product) {
		return productRepository.save(product);
	}
	
	public Optional<Product> findById(Long productId){
		return productRepository.findById(productId);
	}
	
	public Page<Product> findAll(Pageable pageable){
		return productRepository.findAll(pageable);
	}
	
	public boolean existsByNameIgnoreCase(String name) {
		return productRepository.existsByNameIgnoreCase(name);
	}
	
	public void deleteById(Long productId) {
		partService.deleteByProductId(productId);
		productRepository.deleteById(productId);
	}
	
	
	
}
