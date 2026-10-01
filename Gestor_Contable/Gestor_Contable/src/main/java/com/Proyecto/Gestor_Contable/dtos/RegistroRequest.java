package com.Proyecto.Gestor_Contable.dtos;

public record RegistroRequest(
        String nombre,
        String correo,
        String password,
        String preguntaSeguridad

) {}
