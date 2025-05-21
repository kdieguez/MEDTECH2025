package com.medtech.hospitales.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase auxiliar que representa la clave primaria compuesta de la entidad {@link MedicamentoRecetado}.
 * <p>
 * Esta clase combina los identificadores de receta médica ({@code ID_RM}) y medicamento ({@code ID_MEDICAMENTO}),
 * formando una clave única compuesta necesaria para relaciones muchos-a-muchos con atributos adicionales.
 * </p>
 */
public class MedicamentoRecetadoId implements Serializable {

    /** Identificador de la receta médica. */
    private Long receta;

    /** Identificador del medicamento. */
    private Long medicamento;

    /**
     * Obtiene el ID de la receta médica.
     *
     * @return ID de la receta
     */
    public Long getReceta() {
        return receta;
    }

    /**
     * Establece el ID de la receta médica.
     *
     * @param receta ID de la receta
     */
    public void setReceta(Long receta) {
        this.receta = receta;
    }

    /**
     * Obtiene el ID del medicamento.
     *
     * @return ID del medicamento
     */
    public Long getMedicamento() {
        return medicamento;
    }

    /**
     * Establece el ID del medicamento.
     *
     * @param medicamento ID del medicamento
     */
    public void setMedicamento(Long medicamento) {
        this.medicamento = medicamento;
    }

    /**
     * Verifica si dos objetos representan la misma clave compuesta.
     */
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
