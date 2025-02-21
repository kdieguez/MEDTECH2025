package com.hospital.medtech.models;

import java.util.List;

public class Doctor {
    private int id;
    private String nombre;
    private String especialidad;
    private String universidad;
    private String colegiado;
    private String imagen;
    private List<String> titulos;
    private String telefono;

    // Constructor
    public Doctor(int id, String nombre, String especialidad, String universidad, String colegiado, String imagen, List<String> titulos, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.universidad = universidad;
        this.colegiado = colegiado;
        this.imagen = imagen;
        this.titulos = titulos;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }

    public String getColegiado() { return colegiado; }
    public void setColegiado(String colegiado) { this.colegiado = colegiado; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public List<String> getTitulos() { return titulos; }
    public void setTitulos(List<String> titulos) { this.titulos = titulos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
