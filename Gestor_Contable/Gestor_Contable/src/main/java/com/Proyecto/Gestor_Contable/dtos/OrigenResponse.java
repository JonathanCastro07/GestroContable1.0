package com.Proyecto.Gestor_Contable.dtos;

import com.Proyecto.Gestor_Contable.modelo.TipoOrigen;

public record OrigenResponse(
        String id,
        String descripcion,
        TipoOrigen tipoOrigen

) {}
