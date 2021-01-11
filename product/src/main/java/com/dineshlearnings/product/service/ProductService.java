package com.dineshlearnings.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dineshlearnings.product.model.Product;
import com.dineshlearnings.product.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}

	public String addProduct(Product product) {
		productRepository.save(product);
		return "Success!";
	}

	public List<Product> productList() {
		return productRepository.findAll();
	}

	public List<Product> productListCategory(String category) {
		return productRepository.findByCategory(category);
	}

	public Product productById(Integer id) {
		return productRepository.findById(id).get();
	}

	public String updateproduct(Product product) {
		productRepository.save(product);
		return "product update success!";
	}

	public String deleteProductById(Integer id) {
		productRepository.deleteById(id);
		return "ProductDeleted successfully!";
	}
}
