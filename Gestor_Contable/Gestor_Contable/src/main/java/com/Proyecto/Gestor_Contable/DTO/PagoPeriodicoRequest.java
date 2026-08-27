package com.Proyecto.Gestor_Contable.DTO;

import java.time.LocalDate;

public record PagoPeriodicoRequest(
        String nombre,
        Double monto,
        LocalDate fecha,
        String negocioId,
        String tipoMovimientoId,
        String origenId
) {}
