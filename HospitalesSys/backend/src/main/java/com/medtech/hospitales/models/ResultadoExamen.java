package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa una fotografía o imagen asociada a los resultados de un examen médico,
 * vinculada a una cita médica específica.
 */
@Entity
@Table(name = "FOTOGRAFIA_RESULTADOS")
public class ResultadoExamen {

    /** Identificador único de la fotografía del resultado del examen. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FOTORESULT")
    private Long id;

    /** Cita médica a la que pertenece el resultado del examen. */
    @ManyToOne
    @JoinColumn(name = "ID_CITA", nullable = false)
    private CitaMedica cita;

    /** URL o ruta de la imagen almacenada que representa el resultado del examen. */
    @Column(name = "FOTOGRAFIA", nullable = false, length = 500)
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
