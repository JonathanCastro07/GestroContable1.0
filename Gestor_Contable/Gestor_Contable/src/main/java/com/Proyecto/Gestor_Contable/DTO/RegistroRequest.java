package com.Proyecto.Gestor_Contable.DTO;

public record RegistroRequest(
        String nombre,
        String Correo,
        String password,
        String preguntaSeguridad

) {}
