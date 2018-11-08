package com.withkid.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.auth.exception.JwtTypeNotMatchedException;
import com.withkid.auth.exception.UserNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
@RestController
public class ExceptionController {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> userNotFoundHanler() {
		ErrorResponse body = ErrorResponse.builder().name(UserNotFoundException.class.getSimpleName())
				.msg("유저를 찾을 수 없습니다.").status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> tokenExpiredHanler() {
		ErrorResponse body = ErrorResponse.builder().name(ExpiredJwtException.class.getSimpleName())
				.msg("토큰의 유효기간이 초과했습니다.").status(HttpStatus.UNAUTHORIZED).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}

	@ExceptionHandler(JwtTypeNotMatchedException.class)
	public ResponseEntity<ErrorResponse> tokenTypeNotMatchedHanler() {
		ErrorResponse body = ErrorResponse.builder().name(JwtTypeNotMatchedException.class.getSimpleName())
				.msg("토큰의 타입이 불일치합니다.").status(HttpStatus.UNAUTHORIZED).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
	}
}
