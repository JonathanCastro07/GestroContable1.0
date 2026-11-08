package com.Proyecto.Gestor_Contable.dtos;

import java.time.LocalDateTime;

public record MovimientoFinancieroRequest(
        double monto,
        LocalDateTime fecha,
        String descricion,
        String negocioId,
        TipoMovimientoDto tipoMovimiento,
        OrigenDto origen
) {}
