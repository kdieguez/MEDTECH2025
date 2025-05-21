package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) utilizado para representar errores en las respuestas del sistema.
 * <p>
 * Este objeto es retornado al cliente en respuestas HTTP cuando ocurre una excepción
 * o falla en la validación, proporcionando un mensaje descriptivo.
 * </p>
 */
public class ErrorDTO {

    /** Mensaje descriptivo del error. */
    public String error;

    /**
     * Constructor que inicializa el DTO con un mensaje de error.
     *
     * @param error mensaje descriptivo del error
     */
    public ErrorDTO(String error) {
        this.error = error;
    }

    /**
     * Obtiene el mensaje de error.
     *
     * @return mensaje de error
     */
    public String getError() {
        return error;
    }
}
