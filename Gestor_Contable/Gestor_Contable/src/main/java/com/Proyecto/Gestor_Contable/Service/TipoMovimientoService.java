package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.DTO.TipoMovimientoRequest;
import com.Proyecto.Gestor_Contable.DTO.TipoMovimientoResponse;

import java.util.List;

public interface TipoMovimientoService {
    TipoMovimientoResponse crearTipo(TipoMovimientoRequest request);
    List<TipoMovimientoResponse> listarTodo();
    TipoMovimientoResponse buscarPorId(String id);
    TipoMovimientoResponse actualizar(String id, TipoMovimientoRequest request);
    void eliminar(String id);
}
