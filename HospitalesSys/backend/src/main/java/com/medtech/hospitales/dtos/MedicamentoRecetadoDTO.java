package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa un medicamento recetado,
 * utilizado para enviar o recibir datos de recetas médicas
 * entre el frontend y el backend.
 */
public class MedicamentoRecetadoDTO {

    private Long idMedicamento;
    private String dosis;
    private String frecuencia;
    private String duracion;

    // 🔧 Constructor vacío requerido por Jackson
    public MedicamentoRecetadoDTO() {}

    // 🔧 Constructor para consultas o inicialización directa
    public MedicamentoRecetadoDTO(Long idMedicamento, String dosis, String frecuencia, String duracion) {
        this.idMedicamento = idMedicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
    }

    public Long getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Long idMedicamento) {
        this.idMedicamento = idMedicamento;
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
}
