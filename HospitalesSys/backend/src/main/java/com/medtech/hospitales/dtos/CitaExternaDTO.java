package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) utilizado para registrar una cita desde el sistema de seguros.
 * Se espera que se envíen datos mínimos: afiliado, fecha/hora y el nombre de la subcategoría del servicio.
 */
public class CitaExternaDTO {

    /**
     * Fecha y hora programada para la cita, en formato ISO 8601.
     * Ejemplo: "2025-05-20T10:00:00"
     */
    private String fechaHora;

    /**
     * Código del afiliado (ej. "AFI-1234xyz") que se usará para buscar al paciente.
     */
    private String idAfiliado;

    /**
     * Nombre de la subcategoría del servicio solicitado (ej. "Tórax").
     */
    private String nombreSubcategoria;

    // ---------- Getters y Setters ----------

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getIdAfiliado() {
        return idAfiliado;
    }

    public void setIdAfiliado(String idAfiliado) {
        this.idAfiliado = idAfiliado;
    }

    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    public void setNombreSubcategoria(String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }
}
