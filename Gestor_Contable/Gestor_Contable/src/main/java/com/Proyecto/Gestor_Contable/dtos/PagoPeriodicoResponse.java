package com.Proyecto.Gestor_Contable.dtos;

import java.time.LocalDate;

public record PagoPeriodicoResponse(
        String id,
        String nombre,
        Double monto,
        LocalDate fecha,
        Boolean activo,
        String descripcion,
        String negocioId,
        TipoMovimientoDto tipoMovimiento,
        OrigenDto origen
) {}
