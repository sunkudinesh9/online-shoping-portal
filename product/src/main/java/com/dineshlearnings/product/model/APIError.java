package com.dineshlearnings.product.model;

import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class APIError {
	private LocalTime localTime;
	private String pathURL;
	private List<String> errors;
	private HttpStatus httpStatus;

}
