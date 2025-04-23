package com.medtech.hospitales.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.json.JsonMapper;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Implementación personalizada de JsonMapper para Javalin,
 * utilizando ObjectMapper de Jackson para la serialización
 * y deserialización de objetos JSON.
 */
public class CustomJsonMapper implements JsonMapper {

    /**
     * ObjectMapper de Jackson utilizado para procesar JSON.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor que recibe un ObjectMapper configurado.
     *
     * @param objectMapper Instancia de ObjectMapper.
     */
    public CustomJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Deserializa un flujo de entrada JSON a un objeto Java.
     *
     * @param inputStream Flujo de datos de entrada en formato JSON.
     * @param type Tipo de objeto al que se debe deserializar.
     * @param <T> Tipo genérico del objeto resultante.
     * @return Objeto deserializado.
     * @throws RuntimeException si ocurre un error de conversión.
     */
    @Override
    public <T> T fromJsonStream(InputStream inputStream, Type type) {
        try {
            return objectMapper.readValue(inputStream, objectMapper.constructType(type));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializa un objeto Java a una cadena JSON.
     *
     * @param obj Objeto a serializar.
     * @param type Tipo del objeto.
     * @return Representación JSON como cadena de texto.
     * @throws RuntimeException si ocurre un error de serialización.
     */
    @Override
    public String toJsonString(Object obj, Type type) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
