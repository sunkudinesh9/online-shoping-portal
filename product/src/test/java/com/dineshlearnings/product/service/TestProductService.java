package com.dineshlearnings.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
	class ListAllProductsFromTheSystem {
		@Test
		@DisplayName("No products found in the system")
		void productListProductNotFoundException() {
			when(productRepository.findAll()).thenReturn(Collections.emptyList());
			assertThrows(ProductNotFoundException.class, () -> productService.productList());
		}

		@Test
		@DisplayName("happy path - products found in the system")
		void productList() {
			List<Product> expectedProductList = new ArrayList<Product>();
			expectedProductList.add(new Product());
			expectedProductList.add(new Product());
			when(productRepository.findAll()).thenReturn(expectedProductList);
			assertThat(expectedProductList).isEqualTo(productService.productList());

		}

		@Test
		@DisplayName("Product not found for specific category")
		void productCategoryListEmptyTest() {
			when(productRepository.findByCategory(any())).thenReturn(Collections.emptyList());
			assertThrows(ProductNotFoundException.class, () -> productService.productListCategory("Car"));
		}

		@Test
		@DisplayName("Product found for specific category")
		void productCategoryListFromTheSystemTest() {
			List<Product> expectedProductList = new ArrayList<Product>();
			expectedProductList.add(new Product());
			expectedProductList.add(new Product());
			when(productRepository.findByCategory(any())).thenReturn(expectedProductList);
			List<Product> products = productService.productListCategory("Car");
			assertThat(products).isNotNull();
			assertThat(2).isEqualTo(products.size());
		}
	}

	@Nested()
	@DisplayName("All test cases for product to be searched by given Id")
	class ProductByIdTestScenarios {

		@Test
		@DisplayName("Product not found")
		void productByIdNotFoundExceptionTest() {
			assertThrows(ProductNotFoundException.class, () -> productService.productById("random-id"));
		}

		@Test
		@DisplayName("Product found for given id")
		void productByIdTest() {
			Product product = new Product();
			product.setName("Mi TV");
			product.setPrice(10000.0);
			product.setDiscount(10);
			product.setCurrency("INR");

			when(productRepository.findById(any())).thenReturn(Optional.of(product));
			Product actualProduct = productService.productById("random-id");
			assertThat(product).isEqualTo(actualProduct);
		}
	}

	@Nested
	@DisplayName("All the test cases for update product")
	class UpdateProductTestScenarios {

		@Test
		@DisplayName("Product to be updated not found")
		void updateProduct_NotFound() {
			Product product = new Product();
			product.setName("Mi TV");
			product.setPrice(10000.0);
			product.setDiscount(10);
			product.setCurrency("INR");

			ProductResponse actualResponse = productService.updateproduct(product);

			ProductResponse expectedResponse = new ProductResponse("FAILED",
					"Product to be updated not found in the system");

			assertThat(expectedResponse).isEqualTo(actualResponse);
		}

		@Test
		@DisplayName("Product updated - happy path")
		void updateProduct() {
			Product product = new Product();
			product.setId("some-random-id");
			product.setName("Mi TV");
			product.setPrice(10000.0);
			product.setDiscount(10);
			product.setCurrency("INR");

			when(productRepository.findById(any())).thenReturn(Optional.of(product));

			Product newProduct = new Product();
			newProduct.setId(product.getId());
			newProduct.setName("Mi TV New");

			when(productRepository.save(any(Product.class))).thenReturn(newProduct);

			ProductResponse actualResponse = productService.updateproduct(newProduct);

			ProductResponse expectedResponse = new ProductResponse("SUCCESS",
					"Product Updated - " + newProduct.getName());

			assertThat(expectedResponse).isEqualTo(actualResponse);
		}

	}

	@Nested
	@DisplayName("All the test cases for delete product")
	class DeletedProductTestScenarios {
		@Test
		@DisplayName("Product to be deleted not found")
		void deleteProduct_NotFound() {

			ProductResponse actualResponse = productService.deleteProductById("some-random-id");

			ProductResponse expectedResponse = new ProductResponse("FAILED",
					"Product to be deleted not found in the system");

			assertThat(expectedResponse).isEqualTo(actualResponse);
		}

		@Test
		@DisplayName("Product deleted - happy path")
		void deleteProduct() {

			when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));

			ProductResponse actualResponse = productService.deleteProductById("some-random-id");

			ProductResponse expectedResponse = new ProductResponse("SUCCESS", "Product Deleted");

			assertThat(expectedResponse).isEqualTo(actualResponse);

			verify(productRepository, times(1)).deleteById("some-random-id");
		}
	}

}
