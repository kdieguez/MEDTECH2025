package com.medtech.hospitales.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.json.JsonMapper;

import java.io.InputStream;
import java.lang.reflect.Type;

public class CustomJsonMapper implements JsonMapper {

    private final ObjectMapper objectMapper;

    public CustomJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T fromJsonStream(InputStream inputStream, Type type) {
        try {
            return objectMapper.readValue(inputStream, objectMapper.constructType(type));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJsonString(Object obj, Type type) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
