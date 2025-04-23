package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa una subcategoría de un servicio hospitalario,
 * incluyendo su nombre, descripción y precio asociado.
 */
public class SubcategoriaDTO {

    /**
     * Identificador único de la subcategoría.
     */
    private Long id;

    /**
     * Nombre de la subcategoría.
     */
    private String nombre;

    /**
     * Descripción de la subcategoría.
     */
    private String descripcion;

    /**
     * Precio asociado a la subcategoría.
     */
    private Double precio;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public SubcategoriaDTO() {}

    /**
     * Constructor que inicializa todos los atributos de la subcategoría.
     *
     * @param id ID de la subcategoría
     * @param nombre nombre de la subcategoría
     * @param descripcion descripción de la subcategoría
     * @param precio precio de la subcategoría
     */
    public SubcategoriaDTO(Long id, String nombre, String descripcion, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    /**
     * Obtiene el ID de la subcategoría.
     *
     * @return ID de la subcategoría
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la subcategoría.
     *
     * @param id ID de la subcategoría
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la subcategoría.
     *
     * @return nombre de la subcategoría
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la subcategoría.
     *
     * @param nombre nombre de la subcategoría
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción de la subcategoría.
     *
     * @return descripción de la subcategoría
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la subcategoría.
     *
     * @param descripcion descripción de la subcategoría
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio asociado a la subcategoría.
     *
     * @return precio de la subcategoría
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio asociado a la subcategoría.
     *
     * @param precio precio de la subcategoría
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
