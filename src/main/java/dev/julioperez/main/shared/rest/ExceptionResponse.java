package dev.julioperez.main.shared.rest;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String status;
    private int status_code;
    private String exception;

    public ExceptionResponse(String status, int status_code, String exception) {
        this.status = status;
        this.status_code = status_code;
        this.exception = exception;
    }
}

