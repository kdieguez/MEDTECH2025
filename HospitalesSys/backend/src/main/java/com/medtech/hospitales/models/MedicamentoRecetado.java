package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un medicamento recetado en una receta médica.
 */
@Entity
@Table(name = "RECETA_MEDICA")
public class MedicamentoRecetado {

    /**
     * Identificador único del medicamento recetado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO_RECETADO")
    private Long id;

    /**
     * Receta médica a la que pertenece el medicamento recetado.
     */
    @ManyToOne
    @JoinColumn(name = "ID_HEADER_RM", nullable = false)
    private RecetaMedica receta;

    /**
     * Nombre comercial del medicamento recetado.
     */
    @Column(name = "NOMBRE", nullable = false, length = 255)
    private String nombreMedicamento;

    /**
     * Principio activo del medicamento recetado.
     */
    @Column(name = "PRINCIPIO_ACTIVO", nullable = false, length = 255)
    private String principioActivo;

    /**
     * Concentración del medicamento recetado.
     */
    @Column(name = "CONCENTRACION", nullable = false, length = 100)
    private String concentracion;

    /**
     * Presentación del medicamento recetado.
     */
    @Column(name = "PRESENTACION", nullable = false, length = 100)
    private String presentacion;

    /**
     * Forma farmacéutica del medicamento recetado.
     */
    @Column(name = "FORMA_FARMACEUTICA", nullable = true, length = 100)
    private String formaFarmaceutica;

    /**
     * Anotaciones o instrucciones especiales para el medicamento recetado.
     */
    @Column(name = "ANOTACIONES", nullable = true, length = 500)
    private String anotaciones;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RecetaMedica getReceta() { return receta; }
    public void setReceta(RecetaMedica receta) { this.receta = receta; }

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
