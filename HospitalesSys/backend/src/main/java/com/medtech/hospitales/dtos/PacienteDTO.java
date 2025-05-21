package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa la información básica de un paciente.
 * <p>
 * Contiene datos esenciales como nombre, apellido, documento de identificación
 * e identificador único, útil para listados o formularios donde no se requiere el perfil completo.
 * </p>
 */
public class PacienteDTO {

    /** Identificador único del paciente. */
    private Long idPaciente;

    /** Nombre del paciente. */
    private String nombre;

    /** Apellido del paciente. */
    private String apellido;

    /** Documento de identificación del paciente (ej. DPI, pasaporte). */
    private String documentoIdentificacion;

    /**
     * Constructor que inicializa todos los campos del paciente.
     *
     * @param idPaciente ID del paciente
     * @param nombre nombre del paciente
     * @param apellido apellido del paciente
     * @param documentoIdentificacion documento de identificación del paciente
     */
    public PacienteDTO(Long idPaciente, String nombre, String apellido, String documentoIdentificacion) {
        this.idPaciente = idPaciente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documentoIdentificacion = documentoIdentificacion;
    }

    /**
     * Obtiene el ID del paciente.
     *
     * @return ID del paciente
     */
    public Long getIdPaciente() {
        return idPaciente;
    }

    /**
     * Establece el ID del paciente.
     *
     * @param idPaciente ID del paciente
     */
    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * Obtiene el nombre del paciente.
     *
     * @return nombre del paciente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del paciente.
     *
     * @param nombre nombre del paciente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del paciente.
     *
     * @return apellido del paciente
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del paciente.
     *
     * @param apellido apellido del paciente
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el documento de identificación del paciente.
     *
     * @return documento de identificación
     */
    public String getDocumentoIdentificacion() {
        return documentoIdentificacion;
    }

    /**
     * Establece el documento de identificación del paciente.
     *
     * @param documentoIdentificacion documento de identificación
     */
    public void setDocumentoIdentificacion(String documentoIdentificacion) {
        this.documentoIdentificacion = documentoIdentificacion;
    }
}
