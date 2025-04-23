package com.medtech.hospitales.dtos;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) que representa la información resumida de una cita médica,
 * incluyendo datos del paciente, doctor, servicio, subcategoría y fecha de la cita.
 */
public class CitaDTO {

    /**
     * Identificador único de la cita.
     */
    private Long idCita;

    /**
     * Nombre completo del paciente.
     */
    private String nombrePaciente;

    /**
     * DPI (Documento Personal de Identificación) del paciente.
     */
    private String dpiPaciente;

    /**
     * Nombre del doctor asignado a la cita.
     */
    private String nombreDoctor;

    /**
     * Nombre del servicio hospitalario relacionado con la cita.
     */
    private String servicio;

    /**
     * Subcategoría específica del servicio asociado a la cita.
     */
    private String subcategoria;

    /**
     * Fecha y hora programada para la cita médica.
     */
    private LocalDateTime fechaHora;

    /**
     * Constructor para inicializar todos los campos de la cita médica.
     *
     * @param idCita ID de la cita
     * @param nombrePaciente nombre del paciente
     * @param dpiPaciente DPI del paciente
     * @param nombreDoctor nombre del doctor
     * @param servicio nombre del servicio
     * @param subcategoria nombre de la subcategoría
     * @param fechaHora fecha y hora de la cita
     */
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
