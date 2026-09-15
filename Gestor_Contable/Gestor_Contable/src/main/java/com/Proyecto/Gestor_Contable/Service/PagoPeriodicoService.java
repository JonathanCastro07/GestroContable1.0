package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.DTO.PagoPeriodicoRequest;
import com.Proyecto.Gestor_Contable.DTO.PagoPeriodicoResponse;

import java.util.List;

public interface PagoPeriodicoService {
    PagoPeriodicoResponse crear(PagoPeriodicoRequest request);
    List<PagoPeriodicoResponse> listarPorNegocio(String idNegocio);
    List<PagoPeriodicoResponse> listarActivosPorNegocio(String idNegocio);
    List<PagoPeriodicoResponse> listarProximos(String idNegocio);
    PagoPeriodicoResponse buscarPorId(String id);
    PagoPeriodicoResponse actualizar(String id, PagoPeriodicoRequest request);
    void eliminar(String id);
    void ejecutarPago(String id);
    List<PagoPeriodicoResponse> listarPorPeriodo(String idNegocio, Integer mes, Integer anio);
}
