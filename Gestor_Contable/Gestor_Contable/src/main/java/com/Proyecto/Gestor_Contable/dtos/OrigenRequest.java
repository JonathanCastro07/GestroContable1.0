package com.Proyecto.Gestor_Contable.dtos;

import com.Proyecto.Gestor_Contable.modelo.TipoOrigen;

public record OrigenRequest(
        String descripcion,
        TipoOrigen tipoOrigen
) {}
