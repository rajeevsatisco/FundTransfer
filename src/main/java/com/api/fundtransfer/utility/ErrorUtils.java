package com.api.fundtransfer.utility;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling errors and converting BindingResult errors to a formatted string.
 */
public class ErrorUtils {

    /**
     * Converts BindingResult errors into a formatted string representation.
     *
     * @param bindingResult the BindingResult containing validation errors
     * @return a formatted string representing validation errors
     */
    public static String errorsToString(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        // Extract field errors from BindingResult and populate the errors map
        bindingResult.getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Build a formatted string representation of errors
        return formatErrorsToJson(errors);
    }

    private static String formatErrorsToJson(Map<String, String> errors) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        errors.forEach((fieldName, errorMessage) ->
                stringBuilder.append("  ").append(fieldName).append(": ").append(errorMessage).append(","));
        if (!errors.isEmpty()) {
            // Remove the trailing comma and newline
            stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Delete the comma
            stringBuilder.append("");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
