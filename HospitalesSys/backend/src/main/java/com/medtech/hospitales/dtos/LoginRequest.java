package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO (Data Transfer Object) para la solicitud de login en el sistema hospitalario.
 * <p>
 * Contiene las credenciales necesarias: identificador (correo o nombre de usuario)
 * y la contraseña correspondiente.
 * </p>
 */
public class LoginRequest {

    /** Identificador del usuario (correo electrónico o nombre de usuario). */
    @JsonProperty("identificador")
    private String identificador;

    /** Contraseña del usuario. */
    @JsonProperty("contrasena")
    private String contrasena;

    /**
     * Constructor vacío requerido para frameworks de deserialización como Jackson.
     */
    public LoginRequest() {}

    /**
     * Constructor que inicializa los campos del login.
     *
     * @param identificador identificador del usuario (correo o username)
     * @param contrasena contraseña del usuario
     */
    public LoginRequest(String identificador, String contrasena) {
        this.identificador = identificador;
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return identificador (correo o usuario)
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
     * @return contraseña
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
