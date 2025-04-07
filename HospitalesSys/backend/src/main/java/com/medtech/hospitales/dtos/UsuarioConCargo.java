package com.medtech.hospitales.dtos;
import com.medtech.hospitales.models.Usuario;


public class UsuarioConCargo extends Usuario {
    private Integer idCargo;

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }
}
