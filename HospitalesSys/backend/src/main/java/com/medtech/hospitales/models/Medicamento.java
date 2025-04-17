package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "MEDICAMENTOS")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO")
    private Long idMedicamento;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "PRINCIPIO_ACTIVO", nullable = false)
    private String principioActivo;

    @Column(name = "CONCENTRACION", nullable = false)
    private String concentracion;

    @Column(name = "PRESENTACION", nullable = false)
    private String presentacion;

    @Column(name = "FORMA_FARMACEUTICA", nullable = false)
    private String formaFarmaceutica;

    // Getters y Setters

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
}
