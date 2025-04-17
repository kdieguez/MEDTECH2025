package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Objeto de transferencia de datos (DTO) para la solicitud de login. 
 */
public class LoginRequest {
    /** Identificador del usuario (puede ser correo o nombre de usuario). */
    @JsonProperty("identificador")
    private String identificador;

    /** Contraseña del usuario. */
    @JsonProperty("contrasena")
    private String contrasena;

    /** Constructor vacío requerido por Jackson. */
    public LoginRequest() {}

    /**
     * Constructor con todos los campos.
     *
     * @param identificador El correo o nombre de usuario.
     * @param contrasena La contraseña del usuario.
     */

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
