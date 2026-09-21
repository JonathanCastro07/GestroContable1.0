package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.PeriodoRequest;
import com.Proyecto.Gestor_Contable.dtos.PeriodoResponse;
import com.Proyecto.Gestor_Contable.exception.PeriodoNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.PeriodoMapper;
import com.Proyecto.Gestor_Contable.modelo.Periodo;
import com.Proyecto.Gestor_Contable.repository.PeriodoRepository;
import com.Proyecto.Gestor_Contable.service.PeriodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodoServiceImpl implements PeriodoService {

    private final PeriodoRepository periodoRepository;
    private final PeriodoMapper periodoMapper;

    @Override
    public PeriodoResponse crear(PeriodoRequest request) {
        Periodo periodo = periodoMapper.toEntity(request);
        Periodo guardado = periodoRepository.save(periodo);
        return periodoMapper.toResponse(guardado);
    }

    @Override
    public List<PeriodoResponse> listarTodo() {
        return periodoRepository.findAll()
                .stream()
                .map(periodoMapper::toResponse)
                .toList();
    }

    @Override
    public PeriodoResponse buscarPorId(String id) {
        Periodo periodo = periodoRepository.findById(id)
                .orElseThrow(() -> new PeriodoNoEncontradoException("Periodo no encontrado con id: " + id));
        return periodoMapper.toResponse(periodo);
    }

    @Override
    public PeriodoResponse actualizar(String id, PeriodoRequest request) {
        Periodo existente = periodoRepository.findById(id)
                .orElseThrow(() -> new PeriodoNoEncontradoException("Periodo no encontrado con id: " + id));

        existente.setMes(request.mes());
        existente.setAnio(request.anio());

        Periodo actualizado = periodoRepository.save(existente);
        return periodoMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        if (!periodoRepository.existsById(id)) {
            throw new PeriodoNoEncontradoException("Periodo no encontrado con id: " + id);
        }
        periodoRepository.deleteById(id);
    }
}