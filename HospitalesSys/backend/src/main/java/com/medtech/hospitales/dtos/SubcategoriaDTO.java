package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa una subcategoría dentro de un servicio hospitalario.
 * <p>
 * Contiene información como el nombre, descripción y precio asociado,
 * útil para detallar los distintos tipos de atención disponibles dentro de un servicio general.
 * </p>
 */
public class SubcategoriaDTO {

    /** Identificador único de la subcategoría. */
    private Long id;

    /** Nombre descriptivo de la subcategoría (ej. "Radiografía de Tórax", "Consulta Pediátrica"). */
    private String nombre;

    /** Descripción detallada de lo que incluye la subcategoría. */
    private String descripcion;

    /** Precio asignado a la subcategoría del servicio. */
    private Double precio;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public SubcategoriaDTO() {}

    /**
     * Constructor completo para inicializar todos los campos.
     *
     * @param id ID de la subcategoría
     * @param nombre nombre de la subcategoría
     * @param descripcion descripción de la subcategoría
     * @param precio precio asociado
     */
    public SubcategoriaDTO(Long id, String nombre, String descripcion, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
