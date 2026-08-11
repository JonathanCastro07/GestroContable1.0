package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.Modelo.Origen;

import java.util.List;
import java.util.Optional;

public interface OrigenService {
    Origen crear(Origen origen);
    List<Origen> ListarTodo();
    Optional<Origen> busrcarPorId(Long id);
    Origen actualizar(Long id, Origen origen);
    void eliminar(Long id);
}
