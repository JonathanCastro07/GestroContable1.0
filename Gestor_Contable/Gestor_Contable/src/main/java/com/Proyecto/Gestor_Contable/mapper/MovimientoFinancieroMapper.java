package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.Periodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MovimientoFinancieroMapper {
    @Autowired
    private EmbebidosMapper embebidos;

    public MovimientoFinanciero toEntity(MovimientoFinancieroRequest request) {
        LocalDateTime fecha = request.fecha().atStartOfDay();

        MovimientoFinanciero movimiento = new MovimientoFinanciero();
        movimiento.setMonto(request.monto());
        movimiento.setFecha(fecha);
        movimiento.setDescripcion(request.descricion());
        movimiento.setNegocioId(request.negocioId());
        movimiento.setTipoMovimiento(embebidos.toTipo(request.tipoMovimiento()));
        movimiento.setOrigen(embebidos.toOrigen(request.origen()));
        movimiento.setPeriodo(Periodo.de(fecha));
        return movimiento;
    }

    public MovimientoFinancieroResponse toResponse(MovimientoFinanciero movimiento) {
        return new MovimientoFinancieroResponse(
                movimiento.getIdMovimiento(),
                movimiento.getMonto(),
                movimiento.getFecha(),
                movimiento.getDescripcion(),
                movimiento.getNegocioId(),
                embebidos.toDto(movimiento.getTipoMovimiento()),
                embebidos.toDto(movimiento.getOrigen()),
                embebidos.toDto(movimiento.getPeriodo())
        );
    }
}