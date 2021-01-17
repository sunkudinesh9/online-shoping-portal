package com.dineshlearnings.product.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dineshlearnings.product.model.Product;
import com.dineshlearnings.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ProductController")
@RestController
@RequestMapping("/v1")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/addproduct")
	ResponseEntity<Product> addProduct(@RequestBody @Valid Product product) {
		String status = productService.addProduct(product);
		log.info(status);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	@GetMapping("/productlist")
	List<Product> productList() {
		return productService.productList();
	}

	@GetMapping("/productlist/{category}")
	List<Product> productCategotyList(@PathVariable String category) {
		return productService.productListCategory(category);
	}

	@GetMapping("/product/{id}")
	Product productById(@PathVariable String id) {
		return productService.productById(id);
	}

	@PutMapping("/productupdate")
	String productUpdate(@RequestBody @Valid Product product) {
		return productService.updateproduct(product);
	}

	@DeleteMapping("/product/{id}")
	String deleteProductById(@PathVariable String id) {
		return productService.deleteProductById(id);
	}
}
