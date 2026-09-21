package com.Proyecto.Gestor_Contable.service;

import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.dtos.MovimientoResumenResponse;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoService {
    MovimientoFinancieroResponse registrarMovimiento(MovimientoFinancieroRequest request);
    MovimientoFinancieroResponse editarMovimiento(String id, MovimientoFinancieroRequest request);
    MovimientoResumenResponse obtenerResumen(String negocioId, String periodo);
    void eliminarMovimiento(String id);
    List<MovimientoFinancieroResponse> listarPorNegocio(String idNegocio);
    List<MovimientoFinancieroResponse> listarPorNegocioYFecha(String idNegocio, LocalDate desde, LocalDate hasta);
    List<MovimientoFinancieroResponse> listarPorPeriodo(String idNegocio, String mes, Integer anio);
}