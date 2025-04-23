package com.medtech.hospitales.dtos;

import java.util.List;

/**
 * DTO completo que representa un servicio hospitalario
 * con todos los datos necesarios para ser consumido por el sistema de seguros.
 */
public class ServicioDTO {

    private Long id_servicio;
    private String nombre_servicio;
    private String descripcion_servicio;
    private Long id_subcategoria;
    private String nombre_subcategoria;
    private String descripcion_subcategoria;
    private Double precio;
    private List<Long> id_info_doctor;

    public ServicioDTO() {
    }

    public ServicioDTO(Long id_servicio, String nombre_servicio, String descripcion_servicio,
                       Long id_subcategoria, String nombre_subcategoria, String descripcion_subcategoria,
                       Double precio, List<Long> id_info_doctor) {
        this.id_servicio = id_servicio;
        this.nombre_servicio = nombre_servicio;
        this.descripcion_servicio = descripcion_servicio;
        this.id_subcategoria = id_subcategoria;
        this.nombre_subcategoria = nombre_subcategoria;
        this.descripcion_subcategoria = descripcion_subcategoria;
        this.precio = precio;
        this.id_info_doctor = id_info_doctor;
    }

    public Long getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(Long id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombre_servicio() {
        return nombre_servicio;
    }

    public void setNombre_servicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public String getDescripcion_servicio() {
        return descripcion_servicio;
    }

    public void setDescripcion_servicio(String descripcion_servicio) {
        this.descripcion_servicio = descripcion_servicio;
    }

    public Long getId_subcategoria() {
        return id_subcategoria;
    }

    public void setId_subcategoria(Long id_subcategoria) {
        this.id_subcategoria = id_subcategoria;
    }

    public String getNombre_subcategoria() {
        return nombre_subcategoria;
    }

    public void setNombre_subcategoria(String nombre_subcategoria) {
        this.nombre_subcategoria = nombre_subcategoria;
    }

    public String getDescripcion_subcategoria() {
        return descripcion_subcategoria;
    }

    public void setDescripcion_subcategoria(String descripcion_subcategoria) {
        this.descripcion_subcategoria = descripcion_subcategoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public List<Long> getId_info_doctor() {
        return id_info_doctor;
    }

    public void setId_info_doctor(List<Long> id_info_doctor) {
        this.id_info_doctor = id_info_doctor;
    }
}