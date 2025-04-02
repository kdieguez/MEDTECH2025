package com.medtech.hospitales.dtos;

public class NuevaEspecialidadDTO {

    private String nombre;

    public NuevaEspecialidadDTO() {}

    public NuevaEspecialidadDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
