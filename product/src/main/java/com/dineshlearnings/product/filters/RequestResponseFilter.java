package com.dineshlearnings.product.filters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dineshlearnings.product.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RequestResponseFilter implements Filter {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MyCustomHttpRequestMapper requestWrapper = new MyCustomHttpRequestMapper((HttpServletRequest) request);
		String uri = requestWrapper.getRequestURI();
		String requestData = new String(requestWrapper.getByteArray());
		if ("/v1/addproduct".equalsIgnoreCase(uri)) {
			Product readValue = objectMapper.readValue(requestData, Product.class);
			readValue.setCurrency("********");
			requestData = objectMapper.writeValueAsString(readValue);
		}

		log.info("Requeust URI: {}", uri);
		log.info("Requeust Method: {}", requestWrapper.getMethod());
		log.info("Request Body {}: " + requestData);

		MyCustomHttpResponseWrapper responseWrapper = new MyCustomHttpResponseWrapper((HttpServletResponse) response);
		chain.doFilter(requestWrapper, responseWrapper);
		String responseData = new String(responseWrapper.getBaos().toByteArray());
		if ("/v1/addproduct".equalsIgnoreCase(uri)) {
			Product readValue = objectMapper.readValue(responseData, Product.class);
			readValue.setCurrency("********");
			responseData = objectMapper.writeValueAsString(readValue);
		}
		log.info("Response status: {}" + responseWrapper.getStatus());
		log.info("Response status: {}" + responseData);
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

class MyCustomHttpResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private PrintStream printStream = new PrintStream(baos);

	public ByteArrayOutputStream getBaos() {
		return baos;
	}

	public MyCustomHttpResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new MyDelegatingServletOutputStream(new TeeOutputStream(super.getOutputStream(), printStream));
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(new TeeOutputStream(super.getOutputStream(), printStream));
	}
}
