package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.DTO.OrigenRequest;
import com.Proyecto.Gestor_Contable.DTO.OrigenResponse;
import java.util.List;

public interface OrigenService {
    OrigenResponse crear (OrigenRequest request);
    List<OrigenResponse> listarTodo();
    OrigenResponse buscarPorId(String id);
    OrigenResponse actualizar(String id, OrigenRequest request);
    void eliminar(String id);
}
