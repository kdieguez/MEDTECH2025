package com.medtech.hospitales.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * Clase utilitaria para la generación de tokens JWT (JSON Web Tokens)
 * en el sistema hospitalario.
 */
public class JWTUtil {

    /**
     * Llave secreta utilizada para firmar los tokens JWT.
     */
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Tiempo de expiración del token en milisegundos (1 hora).
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * Genera un token JWT que contiene información básica del usuario.
     *
     * @param idUsuario ID del usuario.
     * @param rol Rol del usuario (ID de rol).
     * @param cargo ID del cargo del usuario.
     * @param nombre Nombre del usuario.
     * @param correo Correo electrónico del usuario.
     * @return Token JWT firmado como String.
     */
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
