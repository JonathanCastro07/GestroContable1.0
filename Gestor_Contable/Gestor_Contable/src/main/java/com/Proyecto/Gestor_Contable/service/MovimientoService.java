package com.Proyecto.Gestor_Contable.service;

import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoService {
    MovimientoFinanciero registrarMovimiento(MovimientoFinanciero movimiento);
    MovimientoFinanciero editarMovimiento(Long id, MovimientoFinanciero movimiento);
    void eliminarMovimiento(Long id);
    List<MovimientoFinanciero> listarPorNegocio(Long idNegocio);
    List<MovimientoFinanciero> listarPorNegocioYFecha(Long idNegocio, LocalDate desde, LocalDate hasta);
    List<MovimientoFinanciero> listarPorPeriodo(Long idNegocio, String mes, Integer anio);
}
