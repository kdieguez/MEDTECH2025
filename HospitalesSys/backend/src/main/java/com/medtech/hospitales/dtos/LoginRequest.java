package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO (Data Transfer Object) para la solicitud de login del sistema hospitalario.
 * 
 * Contiene el identificador del usuario (correo o nombre de usuario) y su contraseña.
 */
public class LoginRequest {

    /**
     * Identificador del usuario (puede ser correo electrónico o nombre de usuario).
     */
    @JsonProperty("identificador")
    private String identificador;

    /**
     * Contraseña del usuario.
     */
    @JsonProperty("contrasena")
    private String contrasena;

    /**
     * Constructor vacío requerido para la deserialización automática con Jackson.
     */
    public LoginRequest() {}

    /**
     * Constructor que inicializa todos los campos.
     *
     * @param identificador correo o nombre de usuario
     * @param contrasena contraseña del usuario
     */
    public LoginRequest(String identificador, String contrasena) {
        this.identificador = identificador;
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return identificador del usuario
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param identificador correo o nombre de usuario
     */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return contraseña del usuario
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasena contraseña del usuario
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
