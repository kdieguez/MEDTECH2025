package com.medtech.hospitales.dtos;

public class DoctorResumen {
    private Long id;
    private String nombre;
    private String apellido;
    private String fotografia;

    public DoctorResumen(Long id, String nombre, String apellido, String fotografia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fotografia = fotografia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }
}
