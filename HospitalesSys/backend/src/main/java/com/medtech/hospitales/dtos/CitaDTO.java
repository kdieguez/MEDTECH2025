package com.medtech.hospitales.dtos;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) que representa una vista resumida de una cita médica.
 * <p>
 * Contiene información del paciente, doctor, servicio, subcategoría y fecha programada.
 * Esta clase se utiliza para transferir datos entre capas sin exponer directamente la entidad.
 * </p>
 */
public class CitaDTO {

    /** Identificador único de la cita médica. */
    private Long idCita;

    /** Nombre completo del paciente que asistirá a la cita. */
    private String nombrePaciente;

    /** DPI (Documento Personal de Identificación) del paciente. */
    private String dpiPaciente;

    /** Nombre completo del doctor encargado de la cita. */
    private String nombreDoctor;

    /** Nombre del servicio médico solicitado (ej. Consulta, Laboratorio). */
    private String servicio;

    /** Subcategoría del servicio (ej. Pediatría, Sangre). */
    private String subcategoria;

    /** Fecha y hora programada para la realización de la cita. */
    private LocalDateTime fechaHora;

    /**
     * Constructor completo para inicializar todos los campos de la cita.
     *
     * @param idCita        ID único de la cita médica
     * @param nombrePaciente nombre completo del paciente
     * @param dpiPaciente    DPI del paciente
     * @param nombreDoctor   nombre del doctor
     * @param servicio       nombre del servicio
     * @param subcategoria   subcategoría del servicio
     * @param fechaHora      fecha y hora programadas para la cita
     */
    public CitaDTO(Long idCita, String nombrePaciente, String dpiPaciente,
                   String nombreDoctor, String servicio, String subcategoria,
                   LocalDateTime fechaHora) {
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
