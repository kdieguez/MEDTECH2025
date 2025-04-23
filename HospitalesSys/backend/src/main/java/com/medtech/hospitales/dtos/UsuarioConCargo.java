package com.medtech.hospitales.dtos;

import com.medtech.hospitales.models.Usuario;

/**
 * DTO (Data Transfer Object) que extiende {@link Usuario} para incluir
 * el identificador del cargo asociado al usuario.
 */
public class UsuarioConCargo extends Usuario {

    /**
     * Identificador del cargo asignado al usuario.
     */
    private Integer idCargo;

    /**
     * Obtiene el ID del cargo asociado al usuario.
     *
     * @return ID del cargo
     */
    public Integer getIdCargo() {
        return idCargo;
    }

    /**
     * Establece el ID del cargo asociado al usuario.
     *
     * @param idCargo ID del cargo
     */
    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }
}
