package com.Proyecto.Gestor_Contable.DTO;

import com.Proyecto.Gestor_Contable.Modelo.TipoOrigen;

public record OrigenResponse(
        String id,
        String descripcion,
        TipoOrigen tipoOrigen

) {}
