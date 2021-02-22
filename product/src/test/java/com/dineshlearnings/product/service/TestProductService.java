package com.dineshlearnings.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.dineshlearnings.product.configuration.ProductConfiguration;
import com.dineshlearnings.product.exceptionhandling.CurrencynotValidException;
import com.dineshlearnings.product.exceptionhandling.OfferNotValidException;
import com.dineshlearnings.product.exceptionhandling.ProductNotFoundException;
import com.dineshlearnings.product.model.Product;
import com.dineshlearnings.product.model.ProductResponse;
import com.dineshlearnings.product.repository.ProductRepository;

class TestProductService {

	private ProductRepository productRepository;

	private ProductConfiguration productConfiguration;

	private ProductService productService;

	@BeforeEach
	void setUpBeforeClass() {
		productRepository = Mockito.mock(ProductRepository.class);
		productConfiguration = new ProductConfiguration();
		List<String> currencyList = new ArrayList<>();
		currencyList.add("INR");
		currencyList.add("USD");
		productConfiguration.setCurrency(currencyList);
		productService = new ProductService(productRepository, productConfiguration);

	}

	@Nested
	@DisplayName("All the test cases for adding product into the system")
	class AddProductScenario {
		@Test
		void addProductOfferNotValidException() {
			Product product = new Product();
			product.setDiscount(0);
			product.setPrice(0);
			assertThrows(OfferNotValidException.class, () -> productService.addProduct(product));

		}

		@Test
		void addProductCurrencyNotValidException() {
			Product product = new Product();
			product.setPrice(1000);
			product.setCurrency("ZZZ");
			assertThrows(CurrencynotValidException.class, () -> productService.addProduct(product));
		}

		@Test
		void addProduct() {
			Product product = new Product();
			product.setPrice(1000);
			product.setCurrency("INR");
			when(productRepository.save(any(Product.class))).thenReturn(product);
			ProductResponse actualResponse = productService.addProduct(product);
			ProductResponse expectedResponse = new ProductResponse("success",
					product.getName() + "added into the system");
			assertThat(expectedResponse).isEqualTo(actualResponse);
		}
	}

	@Nested
	@DisplayName("All the test cases for Product List ")
	class ProductListCategort {
		@Test
		void productListProductNotFoundException() {
			when(productRepository.findAll()).thenReturn(Collections.emptyList());
			assertThrows(ProductNotFoundException.class, () -> productService.productList());
		}

		@Test
		void productList() {
			List<Product> expectedProductList = new ArrayList<Product>();
			expectedProductList.add(new Product());
			expectedProductList.add(new Product());
			when(productRepository.findAll()).thenReturn(expectedProductList);
			assertThat(expectedProductList).isEqualTo(productService.productList());

		}
	}

}
