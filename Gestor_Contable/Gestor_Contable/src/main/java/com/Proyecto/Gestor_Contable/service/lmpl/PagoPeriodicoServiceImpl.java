package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoRequest;
import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoResponse;
import com.Proyecto.Gestor_Contable.exception.PagoPeriodicoNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.EmbebidosMapper;
import com.Proyecto.Gestor_Contable.mapper.PagoPeriodicoMapper;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.PagoPeriodico;
import com.Proyecto.Gestor_Contable.modelo.Periodo;
import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.repository.PagoPeriodicoRepository;
import com.Proyecto.Gestor_Contable.service.PagoPeriodicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoPeriodicoServiceImpl implements PagoPeriodicoService {

    @Autowired
    private  PagoPeriodicoRepository pagoRepository;
    @Autowired
    private  MovimientoRepository movimientoRepository;
    @Autowired
    private  PagoPeriodicoMapper pagoPeriodicoMapper;
    @Autowired
    private  EmbebidosMapper embebidos;
    @Autowired
    private  LogAuditoriaServiceImpl logAuditoria;

    @Override
    public PagoPeriodicoResponse crear(PagoPeriodicoRequest request) {
        PagoPeriodico pago = pagoPeriodicoMapper.toEntity(request);
        PagoPeriodico guardado = pagoRepository.save(pago);
        logAuditoria.registrar(guardado.getNegocioId(), "PAGO_PERIODICO_CREADO",
                "Pago periódico '" + guardado.getNombre() + "' por $" + guardado.getMonto());
        return pagoPeriodicoMapper.toResponse(guardado);
    }

    @Override
    public List<PagoPeriodicoResponse> listarPorNegocio(String idNegocio) {
        return pagoRepository.findByNegocioId(idNegocio)
                .stream()
                .map(pagoPeriodicoMapper::toResponse)
                .toList();
    }

    @Override
    public List<PagoPeriodicoResponse> listarActivosPorNegocio(String idNegocio) {
        return pagoRepository.findByNegocioIdAndActivoTrue(idNegocio)
                .stream()
                .map(pagoPeriodicoMapper::toResponse)
                .toList();
    }

    @Override
    public List<PagoPeriodicoResponse> listarProximos(String idNegocio) {
        LocalDate hoy = LocalDate.now();
        LocalDate en3Dias = hoy.plusDays(3);
        return pagoRepository.findByNegocioIdAndActivoTrueAndFechaPagoBetween(idNegocio, hoy, en3Dias)
                .stream()
                .map(pagoPeriodicoMapper::toResponse)
                .toList();
    }

    @Override
    public PagoPeriodicoResponse buscarPorId(String id) {
        PagoPeriodico pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoPeriodicoNoEncontradoException("Pago periódico no encontrado con id: " + id));
        return pagoPeriodicoMapper.toResponse(pago);
    }

    @Override
    public PagoPeriodicoResponse actualizar(String id, PagoPeriodicoRequest request) {
        PagoPeriodico existente = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoPeriodicoNoEncontradoException("Pago periódico no encontrado con id: " + id));

        existente.setNombre(request.nombre());
        existente.setMonto(request.monto());
        existente.setFechaPago(request.fecha());
        existente.setDescripcion(request.descripcion());
        existente.setNegocioId(request.negocioId());
        existente.setTipoMovimiento(embebidos.toTipo(request.tipoMovimiento()));
        existente.setOrigen(embebidos.toOrigen(request.origen()));

        PagoPeriodico actualizado = pagoRepository.save(existente);
        logAuditoria.registrar(actualizado.getNegocioId(), "PAGO_PERIODICO_EDITADO",
                "Pago periódico '" + actualizado.getNombre() + "' editado");
        return pagoPeriodicoMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        PagoPeriodico existente = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoPeriodicoNoEncontradoException("Pago periódico no encontrado con id: " + id));
        pagoRepository.deleteById(id);
        logAuditoria.registrar(existente.getNegocioId(), "PAGO_PERIODICO_ELIMINADO",
                "Pago periódico '" + existente.getNombre() + "' eliminado");
    }

    @Override
    public void ejecutarPago(String id) {
        PagoPeriodico pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoPeriodicoNoEncontradoException("Pago periódico no encontrado con id: " + id));

        LocalDateTime ahora = LocalDateTime.now();

        MovimientoFinanciero movimiento = new MovimientoFinanciero();
        movimiento.setMonto(pago.getMonto());
        movimiento.setFecha(ahora);
        movimiento.setDescripcion("Pago periódico: " + pago.getNombre());
        movimiento.setNegocioId(pago.getNegocioId());
        movimiento.setTipoMovimiento(pago.getTipoMovimiento());
        movimiento.setOrigen(pago.getOrigen());
        movimiento.setPeriodo(Periodo.de(ahora));

        movimientoRepository.save(movimiento);

        pago.setFechaPago(pago.getFechaPago().plusMonths(1));
        pagoRepository.save(pago);

        logAuditoria.registrar(pago.getNegocioId(), "PAGO_PERIODICO_EJECUTADO",
                "Pago periódico '" + pago.getNombre() + "' ejecutado por $" + pago.getMonto());
    }

    @Override
    public List<PagoPeriodicoResponse> listarPorPeriodo(String idNegocio, Integer mes, Integer anio) {
        return pagoRepository.findByNegocioId(idNegocio)
                .stream()
                .filter(pago -> pago.getFechaPago().getMonthValue() == mes
                        && pago.getFechaPago().getYear() == anio)
                .map(pagoPeriodicoMapper::toResponse)
                .toList();
    }
}