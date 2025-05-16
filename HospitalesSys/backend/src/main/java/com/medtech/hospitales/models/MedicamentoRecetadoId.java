package com.medtech.hospitales.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase auxiliar para representar la clave primaria compuesta de MedicamentoRecetado.
 * Ahora usa ID_RM (receta) + ID_MEDICAMENTO como clave compuesta.
 */
public class MedicamentoRecetadoId implements Serializable {

    private Long receta;      // ID_RM en la tabla
    private Long medicamento; // ID_MEDICAMENTO en la tabla

    public Long getReceta() {
        return receta;
    }

    public void setReceta(Long receta) {
        this.receta = receta;
    }

    public Long getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Long medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicamentoRecetadoId)) return false;
        MedicamentoRecetadoId that = (MedicamentoRecetadoId) o;
        return Objects.equals(receta, that.receta) &&
               Objects.equals(medicamento, that.medicamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receta, medicamento);
    }
}
