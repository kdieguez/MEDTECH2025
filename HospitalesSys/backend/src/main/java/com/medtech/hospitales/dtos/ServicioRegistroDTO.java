package com.medtech.hospitales.dtos;

import java.util.List;

public class ServicioRegistroDTO {
    private String nombre;
    private String descripcion;
    private List<SubcategoriaDTO> subcategorias;
    private List<Long> idDoctores;

    public ServicioRegistroDTO() {}

    public ServicioRegistroDTO(String nombre, String descripcion, List<SubcategoriaDTO> subcategorias, List<Long> idDoctores) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.subcategorias = subcategorias;
        this.idDoctores = idDoctores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<SubcategoriaDTO> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<SubcategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public List<Long> getIdDoctores() {
        return idDoctores;
    }

    public void setIdDoctores(List<Long> idDoctores) {
        this.idDoctores = idDoctores;
    }
}
