package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "FOTOGRAFIA_RESULTADOS")
public class ResultadoExamen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FOTORESULT")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CITA", nullable = false)
    private CitaMedica cita;

    @Column(name = "FOTOGRAFIA", nullable = false, length = 500)
    private String url;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CitaMedica getCita() { return cita; }
    public void setCita(CitaMedica cita) { this.cita = cita; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
