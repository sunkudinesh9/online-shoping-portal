package com.dineshlearnings.product.filters;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(1)
public class RequestResponseFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MyCustomHttpRequestMapper requestWrapper = new MyCustomHttpRequestMapper((HttpServletRequest) request);
		log.info("Requeust URI: {}", requestWrapper.getRequestURI());
		log.info("Requeust Method: {}", requestWrapper.getMethod());
		log.info("Request Body {}: " + new String(requestWrapper.getByteArray()));
	}

}

class MyCustomHttpRequestMapper extends HttpServletRequestWrapper {

	private byte[] byteArray;

	public MyCustomHttpRequestMapper(HttpServletRequest request) {
		super(request);
		try {
			byteArray = IOUtils.toByteArray(request.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException("Issue while reading request stream");
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new MyDelegatingServletInputStream(new ByteArrayInputStream(byteArray));
	}

	public byte[] getByteArray() {
		return byteArray;
	}
}
