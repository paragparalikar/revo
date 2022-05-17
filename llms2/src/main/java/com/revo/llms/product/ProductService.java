package com.revo.llms.product;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Service
@RequiredArgsConstructor
public class ProductService {

	@Delegate
	private final ProductRepository productRepository;
	
}
