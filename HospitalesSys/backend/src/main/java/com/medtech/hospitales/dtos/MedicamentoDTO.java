package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa los datos básicos de un medicamento,
 * incluyendo información farmacológica, presentación y notas especiales.
 */
public class MedicamentoDTO {

    /**
     * Nombre comercial del medicamento.
     */
    private String nombre;

    /**
     * Principio activo del medicamento.
     */
    private String principioActivo;

    /**
     * Concentración del medicamento (por ejemplo, 500mg).
     */
    private String concentracion;

    /**
     * Presentación del medicamento (caja, frasco, blister, etc.).
     */
    private String presentacion;

    /**
     * Forma farmacéutica (tableta, inyección, crema, etc.).
     */
    private String formaFarmaceutica;
    
    private Double precio;

    public Double getPrecio() {
        return precio;
    }
    
    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
    /**
     * Obtiene el nombre comercial del medicamento.
     *
     * @return nombre del medicamento
     */
    public String getNombre() { 
        return nombre; 
    }

    /**
     * Establece el nombre comercial del medicamento.
     *
     * @param nombre nombre del medicamento
     */
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    /**
     * Obtiene el principio activo del medicamento.
     *
     * @return principio activo
     */
    public String getPrincipioActivo() { 
        return principioActivo; 
    }

    /**
     * Establece el principio activo del medicamento.
     *
     * @param principioActivo principio activo
     */
    public void setPrincipioActivo(String principioActivo) { 
        this.principioActivo = principioActivo; 
    }

    /**
     * Obtiene la concentración del medicamento.
     *
     * @return concentración del medicamento
     */
    public String getConcentracion() { 
        return concentracion; 
    }

    /**
     * Establece la concentración del medicamento.
     *
     * @param concentracion concentración del medicamento
     */
    public void setConcentracion(String concentracion) { 
        this.concentracion = concentracion; 
    }

    /**
     * Obtiene la presentación del medicamento.
     *
     * @return presentación del medicamento
     */
    public String getPresentacion() { 
        return presentacion; 
    }

    /**
     * Establece la presentación del medicamento.
     *
     * @param presentacion presentación del medicamento
     */
    public void setPresentacion(String presentacion) { 
        this.presentacion = presentacion; 
    }

    /**
     * Obtiene la forma farmacéutica del medicamento.
     *
     * @return forma farmacéutica
     */
    public String getFormaFarmaceutica() { 
        return formaFarmaceutica; 
    }

    /**
     * Establece la forma farmacéutica del medicamento.
     *
     * @param formaFarmaceutica forma farmacéutica
     */
    public void setFormaFarmaceutica(String formaFarmaceutica) { 
        this.formaFarmaceutica = formaFarmaceutica; 
    }

}
