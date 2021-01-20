package com.dineshlearnings.product.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

	@Bean
	FilterRegistrationBean<RequestResponseFilter> filterRequestResponse(RequestResponseFilter requestResponseFilter) {
		FilterRegistrationBean<RequestResponseFilter> filter = new FilterRegistrationBean<RequestResponseFilter>();
		filter.setFilter(requestResponseFilter);
		filter.addUrlPatterns("/v1/addproduct", "/v1/productlist", "/v1/productupdate/*");
		return filter;
	}
}
