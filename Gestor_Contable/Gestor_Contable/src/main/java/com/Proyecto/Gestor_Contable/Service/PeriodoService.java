package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.Modelo.Periodo;

import java.util.List;
import java.util.Optional;

public interface PeriodoService {
    Periodo crear(Periodo periodo);
    List<Periodo> listarTodo();
    Optional<Periodo> BuscarPorId(Long id);
    Periodo actualizar(Long id, Periodo periodo);
    void eliminar(Long id);
}
