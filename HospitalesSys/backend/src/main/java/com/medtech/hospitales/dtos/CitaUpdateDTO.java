package com.medtech.hospitales.dtos;

public class CitaUpdateDTO {
    private String nuevaFechaHora;
    private String nombreSubcategoria;
    private String nombreServicio;

    public String getNuevaFechaHora() {
        return nuevaFechaHora;
    }

    public void setNuevaFechaHora(String nuevaFechaHora) {
        this.nuevaFechaHora = nuevaFechaHora;
    }

    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    public void setNombreSubcategoria(String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }
}
