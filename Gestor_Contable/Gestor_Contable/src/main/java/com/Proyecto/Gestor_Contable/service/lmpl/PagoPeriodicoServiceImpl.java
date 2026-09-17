package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoRequest;
import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoResponse;
import com.Proyecto.Gestor_Contable.exception.PagoPeriodicoNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.PagoPeriodicoMapper;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.PagoPeriodico;
import com.Proyecto.Gestor_Contable.modelo.Periodo;
import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.repository.PagoPeriodicoRepository;
import com.Proyecto.Gestor_Contable.repository.PeriodoRepository;
import com.Proyecto.Gestor_Contable.service.PagoPeriodicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoPeriodicoServiceImpl implements PagoPeriodicoService {

    private final PagoPeriodicoRepository pagoRepository;
    private final MovimientoRepository movimientoRepository;
    private final PeriodoRepository periodoRepository;
    private final PagoPeriodicoMapper pagoPeriodicoMapper;

    @Override
    public PagoPeriodicoResponse crear(PagoPeriodicoRequest request) {
        PagoPeriodico pago = pagoPeriodicoMapper.toEntity(request);
        PagoPeriodico guardado = pagoRepository.save(pago);
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
        existente.setNegocioId(request.negocioId());
        existente.setTipoMovimientoId(request.tipoMovimientoId());
        existente.setOrigenId(request.origenId());

        PagoPeriodico actualizado = pagoRepository.save(existente);
        return pagoPeriodicoMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        if (!pagoRepository.existsById(id)) {
            throw new PagoPeriodicoNoEncontradoException("Pago periódico no encontrado con id: " + id);
        }
        pagoRepository.deleteById(id);
    }

    @Override
    public void ejecutarPago(String id) {
        PagoPeriodico pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoPeriodicoNoEncontradoException("Pago periódico no encontrado con id: " + id));

        MovimientoFinanciero movimiento = new MovimientoFinanciero();
        movimiento.setMonto(pago.getMonto());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion("Pago periódico: " + pago.getNombre());
        movimiento.setNegocioId(pago.getNegocioId());
        movimiento.setTipoMovimientoId(pago.getTipoMovimientoId());
        movimiento.setOrigenId(pago.getOrigenId());

        String mes = LocalDateTime.now().getMonth().name();
        int anio = LocalDateTime.now().getYear();
        Periodo periodo = periodoRepository.findByMesAndAnio(mes, anio)
                .orElseGet(() -> {
                    Periodo nuevoPeriodo = new Periodo();
                    nuevoPeriodo.setMes(mes);
                    nuevoPeriodo.setAnio(anio);
                    return periodoRepository.save(nuevoPeriodo);
                });
        movimiento.setPeriodoId(periodo.getIdPeriodo());

        movimientoRepository.save(movimiento);

        pago.setFechaPago(pago.getFechaPago().plusMonths(1));
        pagoRepository.save(pago);
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