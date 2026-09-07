package com.Proyecto.Gestor_Contable.DTO;

import com.Proyecto.Gestor_Contable.Modelo.TipoOrigen;

public record OrigenRequest(
        String descripcion,
        TipoOrigen tipoOrigen
) {}
