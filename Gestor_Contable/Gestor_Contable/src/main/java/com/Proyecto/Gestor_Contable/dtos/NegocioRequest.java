package com.Proyecto.Gestor_Contable.dtos;

public record NegocioRequest(
        String nombreNegocio,
        String TipoActividad,
        double capitalInicial
) {}
