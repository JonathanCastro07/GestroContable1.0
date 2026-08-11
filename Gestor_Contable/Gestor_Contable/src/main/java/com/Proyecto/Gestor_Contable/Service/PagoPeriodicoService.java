package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.Modelo.PagoPeriodico;
import java.util.List;
import java.util.Optional;

public interface PagoPeriodicoService {
    PagoPeriodico crear(PagoPeriodico pagoPeriodico);
    List<PagoPeriodico> listarPorNegocio(Long idNegocio);
    List<PagoPeriodico> listarActivosPorNegocio(Long idNegocio);
    List<PagoPeriodico> listarProximos(Long idNegocio);
    Optional<PagoPeriodico> buscarPorId(Long id);
    PagoPeriodico actualizar(Long id, PagoPeriodico pagoPeriodico);
    void eliminar(Long id);
    void ejecutarPago(Long id);
    List<PagoPeriodico> listarPorPeriodo(Long idNegocio, Integer mes, Integer anio);
}
