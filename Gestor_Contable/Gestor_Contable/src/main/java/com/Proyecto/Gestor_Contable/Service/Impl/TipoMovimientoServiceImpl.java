package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;
import com.Proyecto.Gestor_Contable.Repository.TipoMovimientoRepository;
import com.Proyecto.Gestor_Contable.Service.TipoMovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class TipoMovimientoServiceImpl implements TipoMovimientoService {

    private final TipoMovimientoRepository tipoMovimientoRepository;

    @Override
    public TipoMovimiento crearTipo(TipoMovimiento tipoMovimiento) {
        return tipoMovimientoRepository.save(tipoMovimiento);
    }

    @Override
    public List<TipoMovimiento> listarTodo() {
        return tipoMovimientoRepository.findAll();
    }

    @Override
    public Optional<TipoMovimiento> BusrcarID(Long id) {
        return tipoMovimientoRepository.findById(id);
    }

    @Override
    public TipoMovimiento actualizar(Long id, TipoMovimiento tipoMovimiento) {
        TipoMovimiento existente = tipoMovimientoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Tipo de movimiento no encontrado"));
        existente.setNombre(tipoMovimiento.getNombre());
        existente.setNaturaleza(tipoMovimiento.getNaturaleza());
        return  tipoMovimientoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        tipoMovimientoRepository.deleteById(id);
    }
}
