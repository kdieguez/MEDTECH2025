package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un medicamento registrado en el sistema hospitalario.
 * <p>
 * Contiene la información necesaria para su identificación y prescripción, incluyendo
 * su nombre, principio activo, concentración, forma farmacéutica, presentación y precio.
 * </p>
 */
@Entity
@Table(name = "MEDICAMENTOS")
public class Medicamento {

    /** Identificador único del medicamento. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO")
    private Long idMedicamento;

    /** Nombre comercial del medicamento. */
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    /** Principio activo del medicamento (ej. paracetamol, amoxicilina). */
    @Column(name = "PRINCIPIO_ACTIVO", nullable = false)
    private String principioActivo;

    /** Concentración del principio activo (ej. "500mg"). */
    @Column(name = "CONCENTRACION", nullable = false)
    private String concentracion;

    /** Presentación del medicamento (ej. caja, frasco, blister). */
    @Column(name = "PRESENTACION", nullable = false)
    private String presentacion;

    /** Forma farmacéutica del medicamento (ej. tableta, inyección, crema). */
    @Column(name = "FORMA_FARMACEUTICA", nullable = false)
    private String formaFarmaceutica;

    /** Precio unitario del medicamento. */
    @Column(name = "PRECIO", nullable = false)
    private Double precio;

    // --- Getters y Setters ---

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrincipioActivo() {
        return principioActivo;
    }

    public void setPrincipioActivo(String principioActivo) {
        this.principioActivo = principioActivo;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
