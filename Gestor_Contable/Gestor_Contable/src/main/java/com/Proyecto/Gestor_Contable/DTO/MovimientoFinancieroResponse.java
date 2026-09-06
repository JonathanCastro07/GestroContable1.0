package com.Proyecto.Gestor_Contable.DTO;

import java.time.LocalDateTime;

public record MovimientoFinancieroResponse(
        String idMovimiento,
        double monto,
        LocalDateTime fecha,
        String descricion,
        String negocioId,
        String tipoId,
        String origenId,
        String periodoId
) {}
