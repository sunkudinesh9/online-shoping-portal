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
import com.dineshlearnings.product.model.ProductResponse;
import com.dineshlearnings.product.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "ProductController")
@RestController
@RequestMapping("/v1")
@Api(description = "This service give the URIs to access the products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/addproduct")
	@ApiOperation("To add the product")
	ResponseEntity<ProductResponse> addProduct(
			@ApiParam("Give the product details") @RequestBody @Valid Product product) {
		ProductResponse productResponse = productService.addProduct(product);
		log.info(productResponse.getStatus());
		return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
	}

	@GetMapping("/productlist")
	List<Product> productList() {
		return productService.productList();
	}

	@GetMapping("/productlist/{category}")
	@ApiOperation("To get the product based on category")
	List<Product> productCategotyList(@PathVariable String category) {
		return productService.productListCategory(category);
	}

	@GetMapping("/product/{id}")
	@ApiOperation("To get the product based on ID")
	Product productById(@PathVariable String id) {
		return productService.productById(id);
	}

	@PutMapping("/productupdate")
	ProductResponse productUpdate(@RequestBody @Valid Product product) {
		return productService.updateproduct(product);
	}

	@DeleteMapping("/product/{id}")
	ProductResponse deleteProductById(@PathVariable String id) {
		return productService.deleteProductById(id);
	}
}
