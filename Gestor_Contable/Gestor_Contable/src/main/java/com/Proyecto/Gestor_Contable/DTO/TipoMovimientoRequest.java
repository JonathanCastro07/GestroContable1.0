package com.Proyecto.Gestor_Contable.DTO;

import com.Proyecto.Gestor_Contable.Modelo.NaturalezaMovimiento;

public record TipoMovimientoRequest(
        String nombre,
        NaturalezaMovimiento naturaleza

) {}
