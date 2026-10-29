package com.Proyecto.Gestor_Contable.dtos;

import com.Proyecto.Gestor_Contable.modelo.TipoOrigen;

public record OrigenDto(
        String id,
        String nombre,
        TipoOrigen tipoOrigen)
{}
