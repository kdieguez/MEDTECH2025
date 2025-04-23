package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa el historial de servicios realizados en una cita médica,
 * incluyendo el diagnóstico y los pasos siguientes recomendados al paciente.
 */
@Entity
@Table(name = "HISTORIAL_SERVICIOS")
public class HistorialServicio {

    /**
     * Identificador único del historial de servicio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORIAL")
    private Long id;

    /**
     * Cita médica asociada al historial de servicio.
     */
    @ManyToOne
    @JoinColumn(name = "ID_CITA", nullable = false)
    private CitaMedica cita;

    /**
     * Diagnóstico realizado durante la cita médica.
     */
    @Column(name = "DIAGNOSTICO")
    private String diagnostico;

    /**
     * Pasos o recomendaciones siguientes tras la cita.
     */
    @Column(name = "SIG_PASOS")
    private String pasosSiguientes;

    /**
     * Obtiene el ID del historial de servicio.
     *
     * @return ID del historial
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del historial de servicio.
     *
     * @param id ID del historial
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene la cita médica asociada.
     *
     * @return cita médica
     */
    public CitaMedica getCita() {
        return cita;
    }

    /**
     * Establece la cita médica asociada.
     *
     * @param cita cita médica
     */
    public void setCita(CitaMedica cita) {
        this.cita = cita;
    }

    /**
     * Obtiene el diagnóstico registrado.
     *
     * @return diagnóstico
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * Establece el diagnóstico registrado.
     *
     * @param diagnostico diagnóstico
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    /**
     * Obtiene los pasos siguientes recomendados al paciente.
     *
     * @return pasos siguientes
     */
    public String getPasosSiguientes() {
        return pasosSiguientes;
    }

    /**
     * Establece los pasos siguientes recomendados al paciente.
     *
     * @param pasosSiguientes pasos siguientes
     */
    public void setPasosSiguientes(String pasosSiguientes) {
        this.pasosSiguientes = pasosSiguientes;
    }
}
