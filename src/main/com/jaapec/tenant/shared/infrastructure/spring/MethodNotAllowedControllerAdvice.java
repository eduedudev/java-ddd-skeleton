package com.jaapec.tenant.shared.infrastructure.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MethodNotAllowedControllerAdvice {

	@RequestMapping
	public ResponseEntity<Void> handleError() {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
	}
}
