package com.Proyecto.Gestor_Contable.dtos;

public record LoginResponse(
        String nombre,
        String correo,
        String token

) {}
