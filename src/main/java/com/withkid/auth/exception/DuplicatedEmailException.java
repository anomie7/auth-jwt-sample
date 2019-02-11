package com.withkid.auth.exception;

public class DuplicatedEmailException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DuplicatedEmailException() {
        super("이미 존재하는 이메일입니다.");
    }

}
