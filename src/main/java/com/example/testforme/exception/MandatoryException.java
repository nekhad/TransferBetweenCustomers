package com.example.testforme.exception;

import java.util.List;

public class MandatoryException extends RuntimeException {
    private final List<String> missingFields;

    public MandatoryException(List<String> missingFields) {
        this.missingFields = missingFields;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }
}