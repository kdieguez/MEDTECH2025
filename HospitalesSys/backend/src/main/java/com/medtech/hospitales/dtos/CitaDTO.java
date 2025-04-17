package com.medtech.hospitales.dtos;

import java.time.LocalDateTime;

public class CitaDTO {
    private Long idCita;
    private String nombrePaciente;
    private String dpiPaciente;
    private String nombreDoctor;
    private String servicio;
    private String subcategoria;
    private LocalDateTime fechaHora;

    public CitaDTO(Long idCita, String nombrePaciente, String dpiPaciente, String nombreDoctor, String servicio, String subcategoria, LocalDateTime fechaHora) {
        this.idCita = idCita;
        this.nombrePaciente = nombrePaciente;
        this.dpiPaciente = dpiPaciente;
        this.nombreDoctor = nombreDoctor;
        this.servicio = servicio;
        this.subcategoria = subcategoria;
        this.fechaHora = fechaHora;
    }

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getDpiPaciente() {
        return dpiPaciente;
    }

    public void setDpiPaciente(String dpiPaciente) {
        this.dpiPaciente = dpiPaciente;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
