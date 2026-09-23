package com.Proyecto.Gestor_Contable.service;


import com.Proyecto.Gestor_Contable.dtos.AnalisisFinancieroIAResponse;
import java.util.List;

public interface AnalisisFinancieroIAService {
    AnalisisFinancieroIAResponse generarAnalisis(String negocioId, String periodo);
    List<AnalisisFinancieroIAResponse> listarPorNegocio(String negocioId);
}
