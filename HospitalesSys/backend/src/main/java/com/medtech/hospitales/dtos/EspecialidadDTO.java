package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa la información de una especialidad médica,
 * incluyendo universidad de graduación, fecha de graduación, título y su fotografía.
 */
public class EspecialidadDTO {

    /**
     * Identificador único de la especialidad.
     */
    private Long idEspecialidad;

    /**
     * Nombre de la universidad donde se obtuvo la especialidad.
     */
    private String universidad;

    /**
     * Fecha de graduación de la especialidad (formato yyyy-MM-dd).
     */
    private String fechaGraduacion;

    /**
     * URL del título universitario relacionado con la especialidad.
     */
    private String urlTitulo;

    /**
     * Nombre de la nueva especialidad (en caso de ser creada en el sistema).
     */
    private String nombreNueva;

    /**
     * URL o referencia de la fotografía del título universitario.
     */
    private String fotografiaTitulo;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public EspecialidadDTO() {}

    /**
     * Constructor que inicializa todos los atributos de la especialidad.
     *
     * @param idEspecialidad ID de la especialidad
     * @param universidad nombre de la universidad
     * @param fechaGraduacion fecha de graduación
     * @param urlTitulo URL del título universitario
     * @param nombreNueva nombre de la nueva especialidad
     * @param fotografiaTitulo URL o referencia de la fotografía del título
     */
    public EspecialidadDTO(Long idEspecialidad, String universidad, String fechaGraduacion, String urlTitulo, String nombreNueva, String fotografiaTitulo) {
        this.idEspecialidad = idEspecialidad;
        this.universidad = universidad;
        this.fechaGraduacion = fechaGraduacion;
        this.urlTitulo = urlTitulo;
        this.nombreNueva = nombreNueva;
        this.fotografiaTitulo = fotografiaTitulo;
    }

    /**
     * Obtiene el ID de la especialidad.
     *
     * @return ID de la especialidad
     */
    public Long getIdEspecialidad() {
        return idEspecialidad;
    }

    /**
     * Establece el ID de la especialidad.
     *
     * @param idEspecialidad ID de la especialidad
     */
    public void setIdEspecialidad(Long idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    /**
     * Obtiene el nombre de la universidad.
     *
     * @return universidad
     */
    public String getUniversidad() {
        return universidad;
    }

    /**
     * Establece el nombre de la universidad.
     *
     * @param universidad nombre de la universidad
     */
    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    /**
     * Obtiene la fecha de graduación.
     *
     * @return fecha de graduación
     */
    public String getFechaGraduacion() {
        return fechaGraduacion;
    }

    /**
     * Establece la fecha de graduación.
     *
     * @param fechaGraduacion fecha de graduación
     */
    public void setFechaGraduacion(String fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    /**
     * Obtiene la URL del título universitario.
     *
     * @return URL del título
     */
    public String getUrlTitulo() {
        return urlTitulo;
    }

    /**
     * Establece la URL del título universitario.
     *
     * @param urlTitulo URL del título
     */
    public void setUrlTitulo(String urlTitulo) {
        this.urlTitulo = urlTitulo;
    }

    /**
     * Obtiene el nombre de la nueva especialidad (si se registró una especialidad nueva).
     *
     * @return nombre de la nueva especialidad
     */
    public String getNombreNueva() {
        return nombreNueva;
    }

    /**
     * Establece el nombre de la nueva especialidad.
     *
     * @param nombreNueva nombre de la nueva especialidad
     */
    public void setNombreNueva(String nombreNueva) {
        this.nombreNueva = nombreNueva;
    }

    /**
     * Obtiene la fotografía del título universitario.
     *
     * @return URL o referencia de la fotografía del título
     */
    public String getFotografiaTitulo() {
        return fotografiaTitulo;
    }

    /**
     * Establece la fotografía del título universitario.
     *
     * @param fotografiaTitulo URL o referencia de la fotografía del título
     */
    public void setFotografiaTitulo(String fotografiaTitulo) {
        this.fotografiaTitulo = fotografiaTitulo;
    }
}
    