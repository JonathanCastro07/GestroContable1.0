package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import org.springframework.stereotype.Component;

@Component
public class MovimientoFinancieroMapper {

    public MovimientoFinanciero toEntity(MovimientoFinancieroRequest request) {
        MovimientoFinanciero movimiento = new MovimientoFinanciero();
        movimiento.setMonto(request.monto());
        movimiento.setFecha(request.fecha().atStartOfDay());
        movimiento.setDescripcion(request.descricion());
        movimiento.setNegocioId(request.negocioId());
        movimiento.setTipoMovimientoId(request.tipoId());
        movimiento.setOrigenId(request.origenId());
        movimiento.setPeriodoId(request.periodoId());
        return movimiento;
    }

    public MovimientoFinancieroResponse toResponse(MovimientoFinanciero movimiento) {
        return new MovimientoFinancieroResponse(
                movimiento.getIdMovimiento(),
                movimiento.getMonto(),
                movimiento.getFecha(),
                movimiento.getDescripcion(),
                movimiento.getNegocioId(),
                movimiento.getTipoMovimientoId(),
                movimiento.getOrigenId(),
                movimiento.getPeriodoId()
        );
    }
}