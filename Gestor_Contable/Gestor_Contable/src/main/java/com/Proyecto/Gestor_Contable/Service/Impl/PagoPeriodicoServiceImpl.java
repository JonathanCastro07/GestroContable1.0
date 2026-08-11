
package com.Proyecto.Gestor_Contable.Service.Impl;


import com.Proyecto.Gestor_Contable.Modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.Modelo.PagoPeriodico;
import com.Proyecto.Gestor_Contable.Modelo.Periodo;
import com.Proyecto.Gestor_Contable.Repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.Repository.PagoPeriodicoRepository;
import com.Proyecto.Gestor_Contable.Repository.PeriodoRepository;
import com.Proyecto.Gestor_Contable.Service.PagoPeriodicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagoPeriodicoServiceImpl implements PagoPeriodicoService {

    @Autowired
    private PagoPeriodicoRepository pagoRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private PeriodoRepository periodoRepository;

    @Override
    public PagoPeriodico crear(PagoPeriodico pagoPeriodico) {
        return pagoRepository.save(pagoPeriodico);
    }

    @Override
    public List<PagoPeriodico> listarPorNegocio(Long idNegocio) {
        return pagoRepository.findByNegocioIdNegocio(idNegocio);
    }

    @Override
    public List<PagoPeriodico> listarActivosPorNegocio(Long idNegocio) {
        return pagoRepository.findByNegocioIdNegocioAndActivoTrue(idNegocio);
    }

    @Override
    public List<PagoPeriodico> listarProximos(Long idNegocio) {
        LocalDate hoy = LocalDate.now();
        LocalDate en3Dias = hoy.plusDays(3);
        return pagoRepository.findByNegocioIdNegocioAndActivoTrueAndFechaPagoBetween(
                idNegocio, hoy, en3Dias);
    }

    @Override
    public Optional<PagoPeriodico> buscarPorId(Long id) {
        return pagoRepository.findById(id);
    }

    @Override
    public PagoPeriodico actualizar(Long id, PagoPeriodico pagoPeriodico) {
        PagoPeriodico existente = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago periódico no encontrado"));
        existente.setNombre(pagoPeriodico.getNombre());
        existente.setMonto(pagoPeriodico.getMonto());
        existente.setFechaPago(pagoPeriodico.getFechaPago());
        existente.setDescripcion(pagoPeriodico.getDescripcion());
        existente.setActivo(pagoPeriodico.isActivo());
        return pagoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }

    @Override
    public void ejecutarPago(Long id) {
        PagoPeriodico pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago periódico no encontrado"));


        MovimientoFinanciero movimiento = new MovimientoFinanciero();
        movimiento.setMonto(pago.getMonto());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion("Pago periódico: " + pago.getNombre());
        movimiento.setNegocio(pago.getNegocio());
        movimiento.setTipoMovimiento(pago.getTipoMovimiento());
        movimiento.setOrigen(pago.getOrigen());


        String mes = LocalDateTime.now().getMonth().name();
        int anio = LocalDateTime.now().getYear();
        Periodo periodo = periodoRepository.findByMesAndAnio(mes, anio)
                .orElseGet(() -> {
                    Periodo nuevoPeriodo = new Periodo();
                    nuevoPeriodo.setMes(mes);
                    nuevoPeriodo.setAnio(anio);
                    return periodoRepository.save(nuevoPeriodo);
                });
        movimiento.setPeriodo(periodo);

        movimientoRepository.save(movimiento);

        pago.setFechaPago(pago.getFechaPago().plusMonths(1));
        pagoRepository.save(pago);
    }

    @Override
    public List<PagoPeriodico> listarPorPeriodo(Long idNegocio, Integer mes, Integer anio) {
        return pagoRepository.findByNegocioAndPeriodo(idNegocio, mes, anio);
    }


}