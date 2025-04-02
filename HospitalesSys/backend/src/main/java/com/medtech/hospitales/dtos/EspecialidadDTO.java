package com.medtech.hospitales.dtos;

public class EspecialidadDTO {

    private Long idEspecialidad;
    private String universidad;
    private String fechaGraduacion;
    private String urlTitulo;
    private String nombreNueva;
    private String fotografiaTitulo;

    public EspecialidadDTO() {}

    public EspecialidadDTO(Long idEspecialidad, String universidad, String fechaGraduacion, String urlTitulo, String nombreNueva, String fotografiaTitulo) {
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
