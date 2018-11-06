package com.withkid.auth.exception;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
		super("유저가 존재하지 않습니다.");
	}
	
}
