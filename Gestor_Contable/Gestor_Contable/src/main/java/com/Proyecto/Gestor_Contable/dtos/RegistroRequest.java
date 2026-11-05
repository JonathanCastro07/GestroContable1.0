package com.Proyecto.Gestor_Contable.dtos;

public record RegistroRequest(
        String nombre,
        String correo,
        String password,
        String telefono,
        String preguntaSeguridad,
        String respuestaSeguridad,
        String nombreNegocio,
        String tipoActividad,
        double capitalInicial,
        String rolPropietario
) {}
