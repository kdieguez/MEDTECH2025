package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    @JsonProperty("identificador")
    private String identificador;

    @JsonProperty("contrasena")
    private String contrasena;

    public LoginRequest() {}

    public LoginRequest(String identificador, String contrasena) {
        this.identificador = identificador;
        this.contrasena = contrasena;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
