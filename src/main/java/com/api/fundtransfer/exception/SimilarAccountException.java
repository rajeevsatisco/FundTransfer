package com.api.fundtransfer.exception;

public class SimilarAccountException extends RuntimeException {
    public SimilarAccountException(String message) {
        super(message);
    }
}