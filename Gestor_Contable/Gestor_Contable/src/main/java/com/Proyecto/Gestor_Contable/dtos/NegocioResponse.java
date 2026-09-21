package com.Proyecto.Gestor_Contable.dtos;

public record NegocioResponse(
        String idNegocio,
        String nombre,
        String tipoActividad,
        double capitalInicial
) {}
