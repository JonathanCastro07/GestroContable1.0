package com.Proyecto.Gestor_Contable.dtos;

import com.Proyecto.Gestor_Contable.modelo.NaturalezaMovimiento;

public record TipoMovimientoDto(
        String id,
        String nombre,
        NaturalezaMovimiento naturaleza)
{}
