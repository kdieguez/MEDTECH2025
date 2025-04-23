package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un medicamento recetado en una receta médica,
 * incluyendo detalles como presentación y anotaciones.
 */
@Entity
@Table(name = "RECETA_MEDICAMENTOS")
public class RecetaMedica {

    /**
     * Identificador único del medicamento recetado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO_RECETA")
    private Long id;

    /**
     * Receta médica a la que pertenece este medicamento.
     */
    @ManyToOne
    @JoinColumn(name = "ID_RECETA")
    private RecetaMedicaHeader receta;

    /**
     * Nombre comercial del medicamento.
     */
    @Column(name = "NOMBRE_MEDICAMENTO")
    private String nombreMedicamento;

    /**
     * Principio activo del medicamento.
     */
    @Column(name = "PRINCIPIO_ACTIVO")
    private String principioActivo;

    /**
     * Concentración del principio activo.
     */
    @Column(name = "CONCENTRACION")
    private String concentracion;

    /**
     * Presentación del medicamento.
     */
    @Column(name = "PRESENTACION")
    private String presentacion;

    /**
     * Forma farmacéutica del medicamento.
     */
    @Column(name = "FORMA_FARMACEUTICA")
    private String formaFarmaceutica;

    /**
     * Anotaciones adicionales sobre el medicamento recetado.
     */
    @Column(name = "ANOTACIONES")
    private String anotaciones;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RecetaMedicaHeader getReceta() { return receta; }
    public void setReceta(RecetaMedicaHeader receta) { this.receta = receta; }

    public String getNombreMedicamento() { return nombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { this.nombreMedicamento = nombreMedicamento; }

    public String getPrincipioActivo() { return principioActivo; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; }

    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getFormaFarmaceutica() { return formaFarmaceutica; }
    public void setFormaFarmaceutica(String formaFarmaceutica) { this.formaFarmaceutica = formaFarmaceutica; }

    public String getAnotaciones() { return anotaciones; }
    public void setAnotaciones(String anotaciones) { this.anotaciones = anotaciones; }
}
