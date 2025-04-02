package com.medtech.hospitales.dtos;

public class ErrorDTO {
    public String error;

    public ErrorDTO(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
