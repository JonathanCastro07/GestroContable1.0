package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.Modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.Modelo.Periodo;
import com.Proyecto.Gestor_Contable.Repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.Repository.PeriodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService implements com.Proyecto.Gestor_Contable.Service.MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private  final PeriodoRepository periodoRepository;

    @Override
    public MovimientoFinanciero registrarMovimiento(MovimientoFinanciero movimiento) {
        if (movimiento.getFecha() == null){
            movimiento.setFecha(LocalDateTime.now());
        }

        String mes = movimiento.getFecha().getMonth().name();
        Integer anio = movimiento.getFecha().getYear();

        Periodo periodo = periodoRepository.findByMesAndAnio(mes, anio)
                .orElseGet(() -> {
                    Periodo nuevoPeriodo = new Periodo();
                    nuevoPeriodo.setMes(mes);
                    nuevoPeriodo.setAnio(anio);
                    return periodoRepository.save(nuevoPeriodo);
                });

        movimiento.setPeriodo(periodo);


        return movimientoRepository.save(movimiento);
    }

    @Override
    public MovimientoFinanciero editarMovimiento(Long id, MovimientoFinanciero movimiento) {
        MovimientoFinanciero movimientoExistente = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));


        movimientoExistente.setMonto(movimiento.getMonto());
        movimientoExistente.setFecha(movimiento.getFecha());
        movimientoExistente.setDescripcion(movimiento.getDescripcion());
        movimientoExistente.setTipoMovimiento(movimiento.getTipoMovimiento());
        movimientoExistente.setOrigen(movimiento.getOrigen());


        return movimientoRepository.save(movimientoExistente);
    }

    @Override
    public void eliminarMovimiento(Long id) {
        movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
        movimientoRepository.deleteById(id);
    }

    @Override
    public List<MovimientoFinanciero> listarPorNegocio(Long idNegocio) {
        return movimientoRepository.findByNegocioIdNegocio(idNegocio);
    }

    @Override
    public List<MovimientoFinanciero> listarPorNegocioYFecha(Long idNegocio, LocalDate desde, LocalDate hasta) {
        return movimientoRepository.findByNegocioIdNegocioAndFechaBetween(idNegocio, desde, hasta);
    }

    @Override
    public List<MovimientoFinanciero> listarPorPeriodo(Long idNegocio, String mes, Integer anio) {
        return movimientoRepository.findByNegocioAndPeriodo(idNegocio, mes, anio);
    }
}

