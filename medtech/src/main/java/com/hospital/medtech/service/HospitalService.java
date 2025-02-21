package com.hospital.medtech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Service
public class HospitalService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int agregarHospital(String nombre) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("MEDTECH")  // <-- Corrige esto
                .withProcedureName("Hospital_Nuevo")
                .declareParameters(
                    new SqlParameter("Nombre_Hospital", Types.VARCHAR),
                    new SqlOutParameter("ID_Hospital_O", Types.NUMERIC)
                );

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("Nombre_Hospital", nombre);

        Map<String, Object> resultado = jdbcCall.execute(parametros);

        return ((Number) resultado.get("ID_Hospital_O")).intValue();
    }
}
