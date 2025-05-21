package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) utilizado para actualizar los datos de una cita médica existente.
 * <p>
 * Permite modificar la fecha/hora, subcategoría y nombre del servicio asociado a la cita.
 * </p>
 */
public class CitaUpdateDTO {

    /** Nueva fecha y hora programada para la cita, en formato ISO 8601. */
    private String nuevaFechaHora;

    /** Nuevo nombre de la subcategoría del servicio (ej. "Cardiología", "Rayos X"). */
    private String nombreSubcategoria;

    /** Nuevo nombre del servicio hospitalario (ej. "Consulta", "Laboratorio"). */
    private String nombreServicio;

    /**
     * Obtiene la nueva fecha y hora de la cita.
     *
     * @return fecha y hora en formato texto ISO 8601
     */
    public String getNuevaFechaHora() {
        return nuevaFechaHora;
    }

    /**
     * Establece la nueva fecha y hora de la cita.
     *
     * @param nuevaFechaHora fecha y hora en formato texto ISO 8601
     */
    public void setNuevaFechaHora(String nuevaFechaHora) {
        this.nuevaFechaHora = nuevaFechaHora;
    }

    /**
     * Obtiene el nombre de la nueva subcategoría del servicio.
     *
     * @return nombre de la subcategoría
     */
    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    /**
     * Establece el nombre de la nueva subcategoría del servicio.
     *
     * @param nombreSubcategoria nombre de la subcategoría
     */
    public void setNombreSubcategoria(String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }

    /**
     * Obtiene el nombre del nuevo servicio asociado.
     *
     * @return nombre del servicio
     */
    public String getNombreServicio() {
        return nombreServicio;
    }

    /**
     * Establece el nombre del nuevo servicio asociado.
     *
     * @param nombreServicio nombre del servicio
     */
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }
}
