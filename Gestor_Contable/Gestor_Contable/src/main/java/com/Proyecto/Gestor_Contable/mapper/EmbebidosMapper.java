package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.OrigenDto;
import com.Proyecto.Gestor_Contable.dtos.PeriodoDto;
import com.Proyecto.Gestor_Contable.dtos.TipoMovimientoDto;
import com.Proyecto.Gestor_Contable.modelo.Origen;
import com.Proyecto.Gestor_Contable.modelo.Periodo;
import com.Proyecto.Gestor_Contable.modelo.TipoMovimiento;
import org.springframework.stereotype.Component;

@Component
public class EmbebidosMapper {

    public TipoMovimiento toTipo(TipoMovimientoDto dto) {
        if (dto == null) return null;
        return TipoMovimiento.nuevo(dto.nombre(), dto.naturaleza());
    }

    public Origen toOrigen(OrigenDto dto) {
        if (dto == null) return null;
        return Origen.nuevo(dto.nombre(), dto.tipoOrigen());
    }

    public TipoMovimientoDto toDto(TipoMovimiento t) {
        if (t == null) return null;
        return new TipoMovimientoDto(t.getIdTipoMovimiento(), t.getNombre(), t.getNaturaleza());
    }

    public OrigenDto toDto(Origen o) {
        if (o == null) return null;
        return new OrigenDto(o.getIdOrigen(), o.getNombre(), o.getTipoOrigen());
    }

    public PeriodoDto toDto(Periodo p) {
        if (p == null) return null;
        return new PeriodoDto(p.getIdPeriodo(), p.getMes(), p.getAnio());
    }
}
