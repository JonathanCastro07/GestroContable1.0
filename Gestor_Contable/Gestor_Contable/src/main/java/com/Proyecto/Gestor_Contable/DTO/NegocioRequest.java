package com.Proyecto.Gestor_Contable.DTO;

public record NegocioRequest(
        String nombreNegocio,
        String TipoActividad,
        double capitalInicial
) {}
