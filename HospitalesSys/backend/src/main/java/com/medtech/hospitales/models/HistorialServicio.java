package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa el historial de atención de una cita médica en el sistema hospitalario.
 * <p>
 * Registra información clínica importante como el diagnóstico emitido por el doctor
 * y las recomendaciones o pasos a seguir por parte del paciente tras la consulta.
 * </p>
 */
@Entity
@Table(name = "HISTORIAL_SERVICIOS")
public class HistorialServicio {

    /** Identificador único del historial de servicio. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORIAL")
    private Long id;

    /** Cita médica asociada a este historial clínico. */
    @ManyToOne
    @JoinColumn(name = "ID_CITA", nullable = false)
    private CitaMedica cita;

    /** Diagnóstico emitido durante la cita médica. */
    @Column(name = "DIAGNOSTICO")
    private String diagnostico;

    /** Pasos siguientes o recomendaciones médicas para el paciente. */
    @Column(name = "SIG_PASOS")
    private String pasosSiguientes;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CitaMedica getCita() {
        return cita;
    }

    public void setCita(CitaMedica cita) {
        this.cita = cita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPasosSiguientes() {
        return pasosSiguientes;
    }

    public void setPasosSiguientes(String pasosSiguientes) {
        this.pasosSiguientes = pasosSiguientes;
    }
}
