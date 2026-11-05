package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoRequest;
import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoResponse;
import com.Proyecto.Gestor_Contable.modelo.PagoPeriodico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PagoPeriodicoMapper {

    @Autowired
    private EmbebidosMapper embebidos;

    public PagoPeriodico toEntity(PagoPeriodicoRequest request) {
        PagoPeriodico pago = new PagoPeriodico();
        pago.setNombre(request.nombre());
        pago.setMonto(request.monto());
        pago.setFechaPago(request.fecha());
        pago.setDescripcion(request.descripcion());
        pago.setNegocioId(request.negocioId());
        pago.setTipoMovimiento(embebidos.toTipo(request.tipoMovimiento()));
        pago.setOrigen(embebidos.toOrigen(request.origen()));
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
                pago.getDescripcion(),
                pago.getNegocioId(),
                embebidos.toDto(pago.getTipoMovimiento()),
                embebidos.toDto(pago.getOrigen())
        );
    }
}