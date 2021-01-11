package com.dineshlearnings.product.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {
	private Integer id;
	@NotNull
	private String name;
	private String brand;

}
