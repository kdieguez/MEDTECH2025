package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) utilizado para registrar una cita desde el sistema de seguros.
 * <p>
 * Contiene los datos mínimos requeridos: código de afiliado, fecha/hora de la cita y
 * nombre de la subcategoría del servicio solicitado.
 * </p>
 */
public class CitaExternaDTO {

    /** Fecha y hora programada para la cita, en formato ISO 8601. Ej: "2025-05-20T10:00:00". */
    private String fechaHora;

    /** Código del afiliado, utilizado para identificar al paciente. Ej: "AFI-1234xyz". */
    private String idAfiliado;

    /** Nombre de la subcategoría del servicio solicitado (ej. "Tórax"). */
    private String nombreSubcategoria;

    // ---------- Getters y Setters ----------

    /**
     * Obtiene la fecha y hora de la cita.
     *
     * @return fecha y hora en formato ISO 8601
     */
    public String getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora de la cita.
     *
     * @param fechaHora fecha y hora en formato ISO 8601
     */
    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene el código de afiliado.
     *
     * @return código de afiliado del paciente
     */
    public String getIdAfiliado() {
        return idAfiliado;
    }

    /**
     * Establece el código de afiliado.
     *
     * @param idAfiliado código de afiliado del paciente
     */
    public void setIdAfiliado(String idAfiliado) {
        this.idAfiliado = idAfiliado;
    }

    /**
     * Obtiene el nombre de la subcategoría del servicio.
     *
     * @return nombre de subcategoría
     */
    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    /**
     * Establece el nombre de la subcategoría del servicio.
     *
     * @param nombreSubcategoria nombre de subcategoría
     */
    public void setNombreSubcategoria(String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }
}
