package com.withkid.auth.controller;

import com.withkid.auth.exception.DuplicatedEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.auth.exception.JwtTypeNotMatchedException;
import com.withkid.auth.exception.PasswordNotMatchException;
import com.withkid.auth.exception.UserNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
@RestController
public class ExceptionController {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> userNotFoundHandler() {
		ErrorResponse body = ErrorResponse.builder().name(UserNotFoundException.class.getSimpleName())
				.msg("유저를 찾을 수 없습니다.").status(HttpStatus.NOT_FOUND).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(DuplicatedEmailException.class)
	public ResponseEntity<ErrorResponse> userExistHandler() {
		ErrorResponse body = ErrorResponse.builder().name(DuplicatedEmailException.class.getSimpleName())
				.msg("같은 이메일이 이미 존재합니다.").status(HttpStatus.CONFLICT).build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
	}

	@ExceptionHandler(PasswordNotMatchException.class)
	public ResponseEntity<ErrorResponse> passwordNotMatchHanler() {
		ErrorResponse body = ErrorResponse.builder().name(PasswordNotMatchException.class.getSimpleName())
				.msg("비밀번호가 일치하지 않습니다.").status(HttpStatus.UNAUTHORIZED).build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
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
