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
	public ResponseEntity<String> userNotFoundHanler(){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
	}
	
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> tokenExpiredHanler(){ 
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰의 유효기간이 초과했습니다.");
	}
	
	@ExceptionHandler(JwtTypeNotMatchedException.class)
	public ResponseEntity<String> tokenTypeNotMatchedHanler(){ 
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰의 타입이 불일치합니다.");
	}
}
