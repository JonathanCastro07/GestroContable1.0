package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.Modelo.Origen;
import com.Proyecto.Gestor_Contable.Repository.OrigenRepository;
import com.Proyecto.Gestor_Contable.Service.OrigenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Proyecto.Gestor_Contable.DTO.OrigenRequest;
import com.Proyecto.Gestor_Contable.DTO.OrigenResponse;
import com.Proyecto.Gestor_Contable.Mapper.OrigenMapper;
import com.Proyecto.Gestor_Contable.Exception.OrigenNoEncontradoException;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class OrigenServiceImpl implements OrigenService {
    private final OrigenRepository origenRepository;
    private final OrigenMapper origenMapper;

    @Override
    public OrigenResponse crear(OrigenRequest request) {
        Origen origen = origenMapper.toEntity(request);
        Origen guardado = origenRepository.save(origen);
        return origenMapper.toResponse(guardado);
    }

    @Override
    public List<OrigenResponse> listarTodo() {
        return origenRepository.findAll()
                .stream()
                .map(origenMapper::toResponse)
                .toList();
    }
    @Override
    public OrigenResponse buscarPorId(String id) {
        Origen origen = origenRepository.findById(id)
                .orElseThrow(() -> new OrigenNoEncontradoException("Origen no encontrado con id: " + id));
        return origenMapper.toResponse(origen);
    }
    @Override
    public OrigenResponse actualizar(String id, OrigenRequest request) {
        Origen existente = origenRepository.findById(id)
                .orElseThrow(() -> new OrigenNoEncontradoException("Origen no encontrado con id: " + id));

        existente.setDescripcion(request.descripcion());
        existente.setTipoOrigen(request.tipoOrigen());

        Origen actualizado = origenRepository.save(existente);
        return origenMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        if (!origenRepository.existsById(id)) {
            throw new OrigenNoEncontradoException("Origen no encontrado con id: " + id);
        }
        origenRepository.deleteById(id);
    }
}
