package com.medtech.hospitales.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;


import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JWTUtilTest {

    @Test
    public void testGenerateToken_containsAllClaims() {
        Long id = 99L;
        Integer rol = 3;
        Integer cargo = 1;
        String nombre = "Daniela";
        String correo = "daniela@example.com";

        String token = JWTUtil.generateToken(id, rol, cargo, nombre, correo);
        assertNotNull(token);

        Key key = getKeyFromJWTUtil(); 

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(id, claims.get("id", Long.class));
        assertEquals(rol, claims.get("rol", Integer.class));
        assertEquals(cargo, claims.get("cargo", Integer.class));
        assertEquals(nombre, claims.get("nombre", String.class));
        assertEquals(correo, claims.get("correo", String.class));

        assertTrue(claims.getExpiration().after(new Date()));
    }

    private Key getKeyFromJWTUtil() {
        try {
            var field = JWTUtil.class.getDeclaredField("key");
            field.setAccessible(true);
            return (Key) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo acceder a la clave secreta para testing", e);
        }
    }

    @Test
public void testTokenFirmaInvalida() {
    String token = Jwts.builder()
            .claim("id", 1)
            .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
            .compact();

    Key keyReal = getKeyFromJWTUtil();

    assertThrows(SignatureException.class, () -> {
        Jwts.parserBuilder()
                .setSigningKey(keyReal)
                .build()
                .parseClaimsJws(token);
    });
}

}
