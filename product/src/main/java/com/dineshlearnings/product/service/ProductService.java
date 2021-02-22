package com.dineshlearnings.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dineshlearnings.product.configuration.ProductConfiguration;
import com.dineshlearnings.product.exceptionhandling.CurrencynotValidException;
import com.dineshlearnings.product.exceptionhandling.OfferNotValidException;
import com.dineshlearnings.product.exceptionhandling.ProductNotFoundException;
import com.dineshlearnings.product.model.Product;
import com.dineshlearnings.product.model.ProductResponse;
import com.dineshlearnings.product.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

	private ProductRepository productRepository;

	private ProductConfiguration productConfiguration;

	public ProductResponse addProduct(Product product) {

		if (product.getPrice() == 0 && product.getDiscount() <= 0) {
			throw new OfferNotValidException("Offers not applicabul for 0 price");
		}

		if (!productConfiguration.getCurrency().contains(product.getCurrency())) {
			throw new CurrencynotValidException(
					"Invalid Currenct: Valid currency are" + productConfiguration.getCurrency());
		}

		Product savedProduct = productRepository.save(product);

		return new ProductResponse("success", savedProduct.getName() + "added into the system");
	}

	public List<Product> productList() {
		List<Product> products = productRepository.findAll();
		if (products.isEmpty()) {
			throw new ProductNotFoundException("No product found for the given query");
		}
		return products;
	}

	public List<Product> productListCategory(String category) {
		List<Product> productsByCategory = productRepository.findByCategory(category);
		if (productsByCategory.isEmpty()) {
			throw new ProductNotFoundException("No product found for the category-" + category);
		}
		return productsByCategory;
	}

	public Product productById(String id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product not found for id - " + id));
	}

	public ProductResponse updateproduct(Product product) {
		Optional<Product> prod = productRepository.findById(product.getId());
		if (!prod.isPresent()) {
			return new ProductResponse("FAILED", "Product to be updated not found in the system");
		}

		Product updatedProduct = productRepository.save(product);

		return new ProductResponse("SUCCESS", "Product Updated - " + updatedProduct.getName());
	}

	public ProductResponse deleteProductById(String id) {
		Optional<Product> prod = productRepository.findById(id);
		if (!prod.isPresent()) {
			return new ProductResponse("FAILED", "Product to be deleted not found in the system");
		}

		productRepository.deleteById(id);

		return new ProductResponse("SUCCESS", "Product Deleted");
	}
}
