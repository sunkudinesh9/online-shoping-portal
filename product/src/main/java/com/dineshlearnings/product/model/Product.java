package com.dineshlearnings.product.model;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "product")
@ApiModel("This entity give the Product detials")
public class Product {
	@Id
	private String id;
	@NotNull
	private String name;
	@NotNull(message = "category is maditory field")
	private Category category;
	@Min(0)
	@ApiModelProperty("Price of the product")
	private double price;
	private String currency;
	@Max(100)
	private double discount;
	private String discountDescription;
	private List<String> imageURLS;

}
