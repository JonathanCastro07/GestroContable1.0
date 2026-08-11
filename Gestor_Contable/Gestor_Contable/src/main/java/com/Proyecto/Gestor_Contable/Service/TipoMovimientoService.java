
package com.Proyecto.Gestor_Contable.Service;
import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;

import java.util.List;
import java.util.Optional;

public interface TipoMovimientoService {
    TipoMovimiento crearTipo(TipoMovimiento tipoMovimiento);
    List<TipoMovimiento> listarTodo();
    Optional<TipoMovimiento> BusrcarID(Long id);
    TipoMovimiento actualizar(Long id, TipoMovimiento tipoMovimiento);
    void eliminar(Long id);
}
