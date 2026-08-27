package com.Proyecto.Gestor_Contable.DTO;

import java.time.LocalDate;

public record PagoPeriodicoResponse(
        String id,
        String nombre,
        Double monto,
        LocalDate fecha,
        Boolean activo,
        String negocioId,
        String tipoMovimientoId,
        String origenId
) {}
