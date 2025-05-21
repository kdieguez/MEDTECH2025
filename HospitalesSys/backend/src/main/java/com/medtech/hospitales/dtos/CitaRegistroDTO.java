package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) utilizado para registrar una nueva cita médica en el sistema.
 * <p>
 * Contiene la información mínima requerida: paciente, doctor, subcategoría del servicio y fecha/hora de la cita.
 * </p>
 */
public class CitaRegistroDTO {

    /** Identificador del paciente que solicita la cita médica. */
    private Long idPaciente;

    /** Identificador del doctor asignado a atender la cita. */
    private Long idDoctor;

    /** Identificador de la subcategoría del servicio solicitado. */
    private Long idSubcategoria;

    /** Fecha y hora programadas para la cita. */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaHora;

    /**
     * Obtiene el ID del paciente.
     *
     * @return ID del paciente
     */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /**
     * Establece el ID del paciente.
     *
     * @param idPaciente ID del paciente
     */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * Obtiene el ID del doctor.
     *
     * @return ID del doctor
     */
    public Long getIdDoctor() {
        return idDoctor;
    }

    /**
     * Establece el ID del doctor.
     *
     * @param idDoctor ID del doctor
     */
    public void setIdDoctor(Long idDoctor) {
        this.idDoctor = idDoctor;
    }

    /**
     * Obtiene el ID de la subcategoría del servicio.
     *
     * @return ID de la subcategoría
     */
    public Long getIdSubcategoria() {
        return idSubcategoria;
    }

    /**
     * Establece el ID de la subcategoría del servicio.
     *
     * @param idSubcategoria ID de la subcategoría
     */
    public void setIdSubcategoria(Long idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    /**
     * Obtiene la fecha y hora programada para la cita.
     *
     * @return fecha y hora de la cita
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora programada para la cita.
     *
     * @param fechaHora fecha y hora de la cita
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
