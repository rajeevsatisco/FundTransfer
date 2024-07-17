package com.api.fundtransfer.constant;
/**
 * Constants class to hold static final variables for various messages and configurations
 * used throughout the Fund Transfer application.
 */
public class Constants {
    public static final String TRANSFER_SUCCESS = "Fund transfer completed successfully";
    public static final String INVALID_CURRENCY_FORMAT = "Invalid currency format";
    public static final String TITLE = "Fund Transfer API";
    public static final String APPLICATION_NAME = "Fund Transfer application";
    public static final String LICENSE = "Fund Transfer application";
    public static final String VERSION = "v0.0.1";
    public static final String URL = "http://springdoc.org";
    public static final String SIMILAR_ACCOUNT_EXCEPTION = "Debit and credit account should not be same for fund transfer";
    public static final String ACCOUNT_NOT_EXIST_EXCEPTION = "Either the debit or the credit account does not exist";
    public static final String EXCHANGE_RATE_RETRIEVAL_EXCEPTION = "The exchange rate cannot be retrieved";
}
