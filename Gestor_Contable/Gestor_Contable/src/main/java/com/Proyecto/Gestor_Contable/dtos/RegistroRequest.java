package com.Proyecto.Gestor_Contable.dtos;

public record RegistroRequest(
        String nombre,
        String Correo,
        String password,
        String preguntaSeguridad

) {}
