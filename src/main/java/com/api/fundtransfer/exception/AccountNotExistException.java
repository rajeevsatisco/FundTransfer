package com.api.fundtransfer.exception;

public class AccountNotExistException extends RuntimeException {
    public AccountNotExistException(String message) {
        super(message);
    }
}
