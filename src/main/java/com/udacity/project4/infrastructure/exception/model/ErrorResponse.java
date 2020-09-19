package com.udacity.project4.infrastructure.exception.model;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<ErrorModel> errors = new ArrayList<>();

    public ErrorResponse() {
    }

    public ErrorResponse(List<ErrorModel> errors) {
        this.errors = errors;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }
}
