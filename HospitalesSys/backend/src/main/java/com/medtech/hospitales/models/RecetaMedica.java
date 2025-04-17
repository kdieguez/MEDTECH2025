package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "RECETA_MEDICAMENTOS")
public class RecetaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO_RECETA")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_RECETA")
    private RecetaMedicaHeader receta;

    @Column(name = "NOMBRE_MEDICAMENTO")
    private String nombreMedicamento;

    @Column(name = "PRINCIPIO_ACTIVO")
    private String principioActivo;

    @Column(name = "CONCENTRACION")
    private String concentracion;

    @Column(name = "PRESENTACION")
    private String presentacion;

    @Column(name = "FORMA_FARMACEUTICA")
    private String formaFarmaceutica;

    @Column(name = "ANOTACIONES")
    private String anotaciones;

    // Getters y setters
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
