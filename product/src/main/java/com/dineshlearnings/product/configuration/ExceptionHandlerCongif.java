package com.dineshlearnings.product.configuration;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dineshlearnings.product.exceptionhandling.CurrencynotValidException;
import com.dineshlearnings.product.exceptionhandling.OfferNotValidException;
import com.dineshlearnings.product.model.APIError;

@ControllerAdvice
public class ExceptionHandlerCongif extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ OfferNotValidException.class, CurrencynotValidException.class })
	ResponseEntity<?> offerNotValidException(Exception exception, ServletWebRequest servletWebRequest) {
		APIError apiError = new APIError();
		apiError.setPathURL(servletWebRequest.getDescription(true));
		apiError.setLocalTime(LocalTime.now());
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setErrors(Arrays.asList(exception.getMessage()));
		return new ResponseEntity<APIError>(apiError, new HttpHeaders(), apiError.getHttpStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> erros = ex.getBindingResult().getFieldErrors();
		List<String> collect = erros.stream().map(err -> err.getField() + ": " + err.getDefaultMessage())
				.collect(Collectors.toList());

		APIError apiError = new APIError();
		apiError.setPathURL(request.getDescription(true));
		apiError.setLocalTime(LocalTime.now());
		apiError.setHttpStatus(HttpStatus.BAD_REQUEST);
		apiError.setErrors(collect);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
	}
}
