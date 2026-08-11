package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.Modelo.Negocio;

import java.util.List;

public interface NegocioServicio {
    Negocio crear(Negocio negocio);
    List<Negocio> listarPorUsuario(Long idUsuario);
    Negocio actualizar (Long id, Negocio negocio);
    void eliminar(Long id);
    double calcularUtilidades(Long idNegocio);
    Object verResumenFinanciero(Long idNegocio);
}
