package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoRequest;
import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoResponse;
import com.Proyecto.Gestor_Contable.modelo.PagoPeriodico;
import org.springframework.stereotype.Component;

@Component
public class PagoPeriodicoMapper {

    public PagoPeriodico toEntity(PagoPeriodicoRequest request) {
        PagoPeriodico pago = new PagoPeriodico();
        pago.setNombre(request.nombre());
        pago.setMonto(request.monto());
        pago.setFechaPago(request.fecha());
        pago.setNegocioId(request.negocioId());
        pago.setTipoMovimientoId(request.tipoMovimientoId());
        pago.setOrigenId(request.origenId());
        pago.setActivo(true);
        return pago;
    }

    public PagoPeriodicoResponse toResponse(PagoPeriodico pago) {
        return new PagoPeriodicoResponse(
                pago.getIdPago(),
                pago.getNombre(),
                pago.getMonto(),
                pago.getFechaPago(),
                pago.isActivo(),
                pago.getNegocioId(),
                pago.getTipoMovimientoId(),
                pago.getOrigenId()
        );
    }
}