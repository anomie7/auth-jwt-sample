package com.withkid.auth.exception;

public class PasswordNotMatchException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2733543526933604022L;

	public PasswordNotMatchException() {
		super("비밀번호가 일치하지 않습니다.");
	}
}
