package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad que representa un medicamento recetado dentro de una receta médica.
 * <p>
 * Forma parte de una relación muchos-a-muchos entre {@link RecetaMedica} y {@link Medicamento},
 * con atributos adicionales como dosis, frecuencia y duración del tratamiento.
 * </p>
 */
@Entity
@Table(name = "MEDICAMENTOS_RECETADOS")
@IdClass(MedicamentoRecetadoId.class)
public class MedicamentoRecetado implements Serializable {

    /** Receta médica a la que pertenece este medicamento. */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RM", referencedColumnName = "ID_RM", nullable = false)
    private RecetaMedica receta;

    /** Medicamento prescrito en la receta. */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MEDICAMENTO", referencedColumnName = "ID_MEDICAMENTO", nullable = false)
    private Medicamento medicamento;

    /** Dosis recomendada para este medicamento (ej. "1 tableta", "5ml"). */
    @Column(name = "DOSIS", nullable = false)
    private String dosis;

    /** Frecuencia con la que debe administrarse el medicamento (ej. "cada 8 horas"). */
    @Column(name = "FRECUENCIA", nullable = false)
    private String frecuencia;

    /** Duración del tratamiento con este medicamento (ej. "7 días"). */
    @Column(name = "DURACION")
    private String duracion;

    /** Constructor vacío requerido por JPA. */
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

    /**
     * Compara dos objetos {@code MedicamentoRecetado} por receta y medicamento.
     */
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
