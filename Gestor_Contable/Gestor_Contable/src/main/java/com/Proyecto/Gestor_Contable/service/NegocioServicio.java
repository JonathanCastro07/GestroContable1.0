package com.Proyecto.Gestor_Contable.service;

import com.Proyecto.Gestor_Contable.dtos.NegocioRequest;
import com.Proyecto.Gestor_Contable.dtos.NegocioResponse;

import java.util.List;

public interface NegocioServicio {
    NegocioResponse crear(NegocioRequest request);
    List<NegocioResponse> listarPorUsuario(String idUsuario);
    NegocioResponse actualizar(String id, NegocioRequest request);
    void eliminar(String id);
    double calcularUtilidades(String idNegocio);
    Object verResumenFinanciero(String idNegocio);
}