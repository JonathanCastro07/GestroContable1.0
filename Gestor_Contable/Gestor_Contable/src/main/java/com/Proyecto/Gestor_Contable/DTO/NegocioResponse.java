package com.Proyecto.Gestor_Contable.DTO;

public record NegocioResponse(
        String idNegocio,
        String nombre,
        String tipoActividad,
        double capitalInicial
) {}
