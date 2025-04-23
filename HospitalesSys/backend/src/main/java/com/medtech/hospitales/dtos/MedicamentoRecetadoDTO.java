package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa un medicamento recetado en una receta médica,
 * incluyendo información como nombre, dosis, frecuencia de administración y duración del tratamiento.
 */
public class MedicamentoRecetadoDTO {

    /**
     * Nombre del medicamento recetado.
     */
    private String nombre;

    /**
     * Dosis prescrita para el medicamento (por ejemplo, 500mg).
     */
    private String dosis;

    /**
     * Frecuencia de administración del medicamento (por ejemplo, "cada 8 horas").
     */
    private String frecuencia;

    /**
     * Duración total del tratamiento (por ejemplo, "7 días").
     */
    private String duracion;

    /**
     * Obtiene el nombre del medicamento recetado.
     *
     * @return nombre del medicamento
     */
    public String getNombre() { 
        return nombre; 
    }

    /**
     * Establece el nombre del medicamento recetado.
     *
     * @param nombre nombre del medicamento
     */
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    /**
     * Obtiene la dosis prescrita del medicamento.
     *
     * @return dosis prescrita
     */
    public String getDosis() { 
        return dosis; 
    }

    /**
     * Establece la dosis prescrita del medicamento.
     *
     * @param dosis dosis prescrita
     */
    public void setDosis(String dosis) { 
        this.dosis = dosis; 
    }

    /**
     * Obtiene la frecuencia de administración del medicamento.
     *
     * @return frecuencia de administración
     */
    public String getFrecuencia() { 
        return frecuencia; 
    }

    /**
     * Establece la frecuencia de administración del medicamento.
     *
     * @param frecuencia frecuencia de administración
     */
    public void setFrecuencia(String frecuencia) { 
        this.frecuencia = frecuencia; 
    }

    /**
     * Obtiene la duración total del tratamiento.
     *
     * @return duración del tratamiento
     */
    public String getDuracion() { 
        return duracion; 
    }

    /**
     * Establece la duración total del tratamiento.
     *
     * @param duracion duración del tratamiento
     */
    public void setDuracion(String duracion) { 
        this.duracion = duracion; 
    }
}
