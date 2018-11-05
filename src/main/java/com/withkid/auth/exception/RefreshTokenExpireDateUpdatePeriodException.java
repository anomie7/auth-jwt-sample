package com.depromeet.team5.exception;

import io.jsonwebtoken.JwtException;

public class RefreshTokenExpireDateUpdatePeriodException extends JwtException {

	public RefreshTokenExpireDateUpdatePeriodException() {
		super("리프레쉬 토큰이 곧 만료될 예정입니다.");
	}

}
