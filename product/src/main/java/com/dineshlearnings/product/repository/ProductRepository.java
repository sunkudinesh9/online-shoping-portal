package com.dineshlearnings.product.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.dineshlearnings.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
	@Query("{'Category.name':?0}")
	List<Product> findByCategory(String category);
}
