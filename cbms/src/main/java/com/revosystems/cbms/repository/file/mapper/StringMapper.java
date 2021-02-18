package com.revosystems.cbms.repository.file.mapper;

public interface StringMapper<T> {

	T map(String text);
	
	String map(T entity);
	
}
