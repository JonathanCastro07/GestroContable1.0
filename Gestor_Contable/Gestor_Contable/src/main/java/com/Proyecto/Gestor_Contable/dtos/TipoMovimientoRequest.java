package com.Proyecto.Gestor_Contable.dtos;

import com.Proyecto.Gestor_Contable.modelo.NaturalezaMovimiento;

public record TipoMovimientoRequest(
        String nombre,
        NaturalezaMovimiento naturaleza

) {}
