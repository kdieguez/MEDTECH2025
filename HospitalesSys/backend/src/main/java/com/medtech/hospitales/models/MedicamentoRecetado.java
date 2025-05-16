package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "MEDICAMENTOS_RECETADOS")
@IdClass(MedicamentoRecetadoId.class)
public class MedicamentoRecetado implements Serializable {

    @Id
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "ID_RM", referencedColumnName = "ID_RM", nullable = false)
private RecetaMedica receta;

@Id
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "ID_MEDICAMENTO", referencedColumnName = "ID_MEDICAMENTO", nullable = false)
private Medicamento medicamento;


    @Column(name = "DOSIS", nullable = false)
    private String dosis;

    @Column(name = "FRECUENCIA", nullable = false)
    private String frecuencia;

    @Column(name = "DURACION")
    private String duracion;

    // Getters y Setters...
    public MedicamentoRecetado() {}

    public RecetaMedica getReceta() {
        return receta;
    }

    public void setReceta(RecetaMedica receta) {
        this.receta = receta;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicamentoRecetado)) return false;
        MedicamentoRecetado that = (MedicamentoRecetado) o;
        return Objects.equals(receta, that.receta) && Objects.equals(medicamento, that.medicamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receta, medicamento);
    }
}
