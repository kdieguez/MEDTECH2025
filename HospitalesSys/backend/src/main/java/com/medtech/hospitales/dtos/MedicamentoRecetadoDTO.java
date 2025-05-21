package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa un medicamento recetado en una receta médica.
 * <p>
 * Se utiliza para transferir información entre frontend y backend sobre
 * la administración de un medicamento prescrito, incluyendo dosis, frecuencia y duración.
 * </p>
 */
public class MedicamentoRecetadoDTO {

    /** Identificador del medicamento recetado. */
    private Long idMedicamento;

    /** Dosis recomendada del medicamento (ej. "1 tableta", "5ml"). */
    private String dosis;

    /** Frecuencia con la que debe tomarse el medicamento (ej. "cada 8 horas"). */
    private String frecuencia;

    /** Duración del tratamiento (ej. "7 días", "2 semanas"). */
    private String duracion;

    /**
     * Constructor vacío requerido para serialización/deserialización.
     */
    public MedicamentoRecetadoDTO() {}

    /**
     * Constructor para inicializar todos los campos.
     *
     * @param idMedicamento ID del medicamento
     * @param dosis dosis indicada
     * @param frecuencia frecuencia de administración
     * @param duracion duración del tratamiento
     */
    public MedicamentoRecetadoDTO(Long idMedicamento, String dosis, String frecuencia, String duracion) {
        this.idMedicamento = idMedicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
    }

    /**
     * Obtiene el ID del medicamento recetado.
     *
     * @return ID del medicamento
     */
    public Long getIdMedicamento() {
        return idMedicamento;
    }

    /**
     * Establece el ID del medicamento recetado.
     *
     * @param idMedicamento ID del medicamento
     */
    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    /**
     * Obtiene la dosis del medicamento.
     *
     * @return dosis del medicamento
     */
    public String getDosis() {
        return dosis;
    }

    /**
     * Establece la dosis del medicamento.
     *
     * @param dosis dosis del medicamento
     */
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    /**
     * Obtiene la frecuencia con la que se debe administrar el medicamento.
     *
     * @return frecuencia de administración
     */
    public String getFrecuencia() {
        return frecuencia;
    }

    /**
     * Establece la frecuencia de administración del medicamento.
     *
     * @param frecuencia frecuencia de administración
     */
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    /**
     * Obtiene la duración total del tratamiento.
     *
     * @return duración del tratamiento
     */
    public String getDuracion() {
        return duracion;
    }

    /**
     * Establece la duración del tratamiento.
     *
     * @param duracion duración del tratamiento
     */
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
