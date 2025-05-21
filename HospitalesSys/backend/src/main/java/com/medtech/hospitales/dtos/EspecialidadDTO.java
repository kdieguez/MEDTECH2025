package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa la información detallada de una especialidad médica.
 * <p>
 * Incluye datos como universidad, fecha de graduación, título universitario,
 * nombre de la especialidad (nueva o existente) y una fotografía del título.
 * </p>
 */
public class EspecialidadDTO {

    /** Identificador único de la especialidad médica. */
    private Long idEspecialidad;

    /** Nombre de la universidad donde se obtuvo la especialidad. */
    private String universidad;

    /** Fecha de graduación en formato yyyy-MM-dd. */
    private String fechaGraduacion;

    /** URL del título universitario relacionado con la especialidad. */
    private String urlTitulo;

    /** Nombre de la especialidad si fue creada como nueva en el sistema. */
    private String nombreNueva;

    /** URL o referencia de la fotografía del título universitario. */
    private String fotografiaTitulo;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public EspecialidadDTO() {}

    /**
     * Constructor completo que inicializa todos los campos de la especialidad.
     *
     * @param idEspecialidad ID de la especialidad
     * @param universidad nombre de la universidad
     * @param fechaGraduacion fecha de graduación
     * @param urlTitulo URL del título
     * @param nombreNueva nombre de nueva especialidad
     * @param fotografiaTitulo URL o referencia de la fotografía del título
     */
    public EspecialidadDTO(Long idEspecialidad, String universidad, String fechaGraduacion,
                           String urlTitulo, String nombreNueva, String fotografiaTitulo) {
        this.idEspecialidad = idEspecialidad;
        this.universidad = universidad;
        this.fechaGraduacion = fechaGraduacion;
        this.urlTitulo = urlTitulo;
        this.nombreNueva = nombreNueva;
        this.fotografiaTitulo = fotografiaTitulo;
    }

    public Long getIdEspecialidad() {
        return idEspecialidad;
    }

    public void setIdEspecialidad(Long idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getFechaGraduacion() {
        return fechaGraduacion;
    }

    public void setFechaGraduacion(String fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    public String getUrlTitulo() {
        return urlTitulo;
    }

    public void setUrlTitulo(String urlTitulo) {
        this.urlTitulo = urlTitulo;
    }

    public String getNombreNueva() {
        return nombreNueva;
    }

    public void setNombreNueva(String nombreNueva) {
        this.nombreNueva = nombreNueva;
    }

    public String getFotografiaTitulo() {
        return fotografiaTitulo;
    }

    public void setFotografiaTitulo(String fotografiaTitulo) {
        this.fotografiaTitulo = fotografiaTitulo;
    }
}
