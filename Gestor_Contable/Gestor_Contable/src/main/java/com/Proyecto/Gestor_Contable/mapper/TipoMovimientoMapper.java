package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.TipoMovimientoRequest;
import com.Proyecto.Gestor_Contable.dtos.TipoMovimientoResponse;
import com.Proyecto.Gestor_Contable.modelo.TipoMovimiento;
import org.springframework.stereotype.Component;

@Component
public class TipoMovimientoMapper {

    public TipoMovimiento toEntity(TipoMovimientoRequest request) {
        TipoMovimiento tipoMovimiento = new TipoMovimiento();
        tipoMovimiento.setNombre(request.nombre());
        tipoMovimiento.setNaturaleza(request.naturaleza());
        return tipoMovimiento;
    }

    public TipoMovimientoResponse toResponse(TipoMovimiento tipoMovimiento) {
        return new TipoMovimientoResponse(
                tipoMovimiento.getIdTipoMovimiento(),
                tipoMovimiento.getNombre(),
                tipoMovimiento.getNaturaleza().name()
        );
    }
}