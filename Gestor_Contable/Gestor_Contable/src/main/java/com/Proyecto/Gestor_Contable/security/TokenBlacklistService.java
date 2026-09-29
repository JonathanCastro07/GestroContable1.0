package com.Proyecto.Gestor_Contable.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void agregarABlacklist(String token, long segundosRestantes) {
        redisTemplate.opsForValue().set(token, "invalidado", segundosRestantes, TimeUnit.SECONDS);
    }

    public boolean estaEnBlacklist(String token) {
        return redisTemplate.hasKey(token);
    }
}