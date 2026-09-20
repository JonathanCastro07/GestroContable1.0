package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.DTO.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.DTO.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.DTO.MovimientoResumenResponse;

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