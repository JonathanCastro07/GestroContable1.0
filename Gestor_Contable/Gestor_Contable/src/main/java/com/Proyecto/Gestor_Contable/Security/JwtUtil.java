package com.Proyecto.Gestor_Contable.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretkey;

    @Value("${jwt.expiration}")
    private Long expiracion;

    public  String generarToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(getSignKey())
                .compact();
    }

    public String extraerEmail(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validaToken(String token, UserDetails userDetails){
        String email = extraerEmail(token);
        return email.equals(userDetails.getUsername()) && !tokenExpirado(token);

    }

    private boolean tokenExpirado(String token){
        Date fechaExpiracion = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return fechaExpiracion.before(new Date());
    }


    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
