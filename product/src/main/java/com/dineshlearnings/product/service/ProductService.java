package com.dineshlearnings.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dineshlearnings.product.configuration.ProductConfiguration;
import com.dineshlearnings.product.exceptionhandling.CurrencynotValidException;
import com.dineshlearnings.product.exceptionhandling.OfferNotValidException;
import com.dineshlearnings.product.model.Product;
import com.dineshlearnings.product.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

	private ProductRepository productRepository;

	private ProductConfiguration productConfiguration;

	public String addProduct(Product product) {

		if (!productConfiguration.getCurrency().contains(product.getCurrency())) {
			throw new CurrencynotValidException(
					"Invalid Currenct: Valid currency are" + productConfiguration.getCurrency());
		}

		if (product.getPrice() == 0 && product.getDiscount() > 0) {
			throw new OfferNotValidException("Offers not applicabul for 0 price");
		}
		productRepository.save(product);
		return "Success!";
	}

	public List<Product> productList() {
		return productRepository.findAll();
	}

	public List<Product> productListCategory(String category) {
		return productRepository.findByCategory(category);
	}

	public Product productById(String id) {
		return productRepository.findById(id).get();
	}

	public String updateproduct(Product product) {
		productRepository.save(product);
		return "product update success!";
	}

	public String deleteProductById(String id) {
		productRepository.deleteById(id);
		return "ProductDeleted successfully!";
	}
}
