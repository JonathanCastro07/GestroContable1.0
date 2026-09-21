package com.Proyecto.Gestor_Contable.service;

import com.Proyecto.Gestor_Contable.dtos.PeriodoRequest;
import com.Proyecto.Gestor_Contable.dtos.PeriodoResponse;

import java.util.List;

public interface PeriodoService {
    PeriodoResponse crear(PeriodoRequest request);
    List<PeriodoResponse> listarTodo();
    PeriodoResponse buscarPorId(String id);
    PeriodoResponse actualizar(String id, PeriodoRequest request);
    void eliminar(String id);
}