package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "RECETA_MEDICA")
public class MedicamentoRecetado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO_RECETADO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_HEADER_RM", nullable = false)
    private RecetaMedica receta;

    @Column(name = "NOMBRE", nullable = false, length = 255)
    private String nombreMedicamento;

    @Column(name = "PRINCIPIO_ACTIVO", nullable = false, length = 255)
    private String principioActivo;

    @Column(name = "CONCENTRACION", nullable = false, length = 100)
    private String concentracion;

    @Column(name = "PRESENTACION", nullable = false, length = 100)
    private String presentacion;

    @Column(name = "FORMA_FARMACEUTICA", nullable = true, length = 100)
    private String formaFarmaceutica;

    @Column(name = "ANOTACIONES", nullable = true, length = 500)
    private String anotaciones;

    // Getters y setters
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
