package com.Proyecto.Gestor_Contable.Mapper;

import com.Proyecto.Gestor_Contable.DTO.TipoMovimientoRequest;
import com.Proyecto.Gestor_Contable.DTO.TipoMovimientoResponse;
import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;
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