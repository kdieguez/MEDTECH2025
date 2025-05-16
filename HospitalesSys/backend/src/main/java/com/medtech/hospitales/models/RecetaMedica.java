package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa la receta médica asociada a una cita.
 */
@Entity
@Table(name = "RECETA_MEDICA")
public class RecetaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receta_seq")
    @SequenceGenerator(name = "receta_seq", sequenceName = "SEQ_ID_RM", allocationSize = 1)
    @Column(name = "ID_RM")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CITAS_MEDICAS", referencedColumnName = "ID_CITA", nullable = false)
    private CitaMedica citaMedica;

    @Column(name = "CODIGO_RECETA", nullable = false, unique = true)
    private String codigoReceta;

    @Column(name = "ANOTACIONES")
    private String anotaciones;

    @Column(name = "TOTAL")
    private Double total;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicamentoRecetado> medicamentos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CitaMedica getCitaMedica() {
        return citaMedica;
    }

    public void setCitaMedica(CitaMedica citaMedica) {
        this.citaMedica = citaMedica;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<MedicamentoRecetado> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoRecetado> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public void addMedicamentoRecetado(MedicamentoRecetado med) {
        med.setReceta(this); 
        this.medicamentos.add(med);
    }
}
