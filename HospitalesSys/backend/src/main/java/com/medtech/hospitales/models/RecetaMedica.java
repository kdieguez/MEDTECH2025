package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una receta médica asociada a una cita en el sistema hospitalario.
 * <p>
 * Incluye un código único de receta, anotaciones médicas, el total de medicamentos recetados
 * y una relación directa con una {@link CitaMedica}.
 * </p>
 */
@Entity
@Table(name = "RECETA_MEDICA")
public class RecetaMedica {

    /** Identificador único de la receta médica. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receta_seq")
    @SequenceGenerator(name = "receta_seq", sequenceName = "SEQ_ID_RM", allocationSize = 1)
    @Column(name = "ID_RM")
    private Long id;

    /** Cita médica a la que está vinculada esta receta. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CITAS_MEDICAS", referencedColumnName = "ID_CITA", nullable = false)
    private CitaMedica citaMedica;

    /** Código único que identifica la receta (ej. "HOSP-SEG-123"). */
    @Column(name = "CODIGO_RECETA", nullable = false, unique = true)
    private String codigoReceta;

    /** Anotaciones adicionales del doctor (instrucciones, advertencias, etc.). */
    @Column(name = "ANOTACIONES")
    private String anotaciones;

    /** Total monetario estimado de los medicamentos incluidos. */
    @Column(name = "TOTAL")
    private Double total;

    /** Lista de medicamentos recetados en esta receta. */
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicamentoRecetado> medicamentos = new ArrayList<>();

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CitaMedica getCitaMedica() {
        return citaMedica;
    }

    public void setCitaMedica(CitaMedica citaMedica) {
        this.citaMedica = citaMedica;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<MedicamentoRecetado> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoRecetado> medicamentos) {
        this.medicamentos = medicamentos;
    }

    /**
     * Agrega un medicamento recetado a esta receta.
     * Se asegura de establecer la relación bidireccional correctamente.
     *
     * @param med objeto {@link MedicamentoRecetado} que se agregará
     */
    public void addMedicamentoRecetado(MedicamentoRecetado med) {
        med.setReceta(this);
        this.medicamentos.add(med);
    }
}
