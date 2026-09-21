package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.TipoMovimientoRequest;
import com.Proyecto.Gestor_Contable.dtos.TipoMovimientoResponse;
import com.Proyecto.Gestor_Contable.exception.TipoMovimientoNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.TipoMovimientoMapper;
import com.Proyecto.Gestor_Contable.modelo.TipoMovimiento;
import com.Proyecto.Gestor_Contable.repository.TipoMovimientoRepository;
import com.Proyecto.Gestor_Contable.service.TipoMovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoMovimientoServiceImpl implements TipoMovimientoService {

    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final TipoMovimientoMapper tipoMovimientoMapper;

    @Override
    public TipoMovimientoResponse crearTipo(TipoMovimientoRequest request) {
        TipoMovimiento tipoMovimiento = tipoMovimientoMapper.toEntity(request);
        TipoMovimiento guardado = tipoMovimientoRepository.save(tipoMovimiento);
        return tipoMovimientoMapper.toResponse(guardado);
    }

    @Override
    public List<TipoMovimientoResponse> listarTodo() {
        return tipoMovimientoRepository.findAll()
                .stream()
                .map(tipoMovimientoMapper::toResponse)
                .toList();
    }

    @Override
    public TipoMovimientoResponse buscarPorId(String id) {
        TipoMovimiento tipoMovimiento = tipoMovimientoRepository.findById(id)
                .orElseThrow(() -> new TipoMovimientoNoEncontradoException("Tipo de movimiento no encontrado con id: " + id));
        return tipoMovimientoMapper.toResponse(tipoMovimiento);
    }

    @Override
    public TipoMovimientoResponse actualizar(String id, TipoMovimientoRequest request) {
        TipoMovimiento existente = tipoMovimientoRepository.findById(id)
                .orElseThrow(() -> new TipoMovimientoNoEncontradoException("Tipo de movimiento no encontrado con id: " + id));

        existente.setNombre(request.nombre());
        existente.setNaturaleza(request.naturaleza());

        TipoMovimiento actualizado = tipoMovimientoRepository.save(existente);
        return tipoMovimientoMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        if (!tipoMovimientoRepository.existsById(id)) {
            throw new TipoMovimientoNoEncontradoException("Tipo de movimiento no encontrado con id: " + id);
        }
        tipoMovimientoRepository.deleteById(id);
    }
}
