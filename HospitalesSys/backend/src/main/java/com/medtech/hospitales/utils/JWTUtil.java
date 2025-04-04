package com.medtech.hospitales.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JWTUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; 

    public static String generateToken(Long idUsuario, Integer rol, Integer cargo, String nombre, String correo) {
        return Jwts.builder()
                .claim("id", idUsuario)
                .claim("rol", rol)
                .claim("cargo", cargo)
                .claim("nombre", nombre)
                .claim("correo", correo)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}
