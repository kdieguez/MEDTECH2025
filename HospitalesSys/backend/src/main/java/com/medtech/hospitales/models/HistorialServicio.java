package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "HISTORIAL_SERVICIOS")
public class HistorialServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HISTORIAL")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CITA", nullable = false)
    private CitaMedica cita;

    @Column(name = "DIAGNOSTICO")
    private String diagnostico;

    @Column(name = "SIG_PASOS")
    private String pasosSiguientes;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CitaMedica getCita() { return cita; }
    public void setCita(CitaMedica cita) { this.cita = cita; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getPasosSiguientes() { return pasosSiguientes; }
    public void setPasosSiguientes(String pasosSiguientes) { this.pasosSiguientes = pasosSiguientes; }
}
