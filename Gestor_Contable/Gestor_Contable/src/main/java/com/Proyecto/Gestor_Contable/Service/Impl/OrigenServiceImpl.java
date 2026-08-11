package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.Modelo.Origen;
import com.Proyecto.Gestor_Contable.Repository.OrigenRepository;
import com.Proyecto.Gestor_Contable.Service.OrigenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class OrigenServiceImpl implements OrigenService {
    @Autowired
    private OrigenRepository origenRepository;

    @Override
    public Origen crear(Origen origen) {
       return  origenRepository.save(origen);
    }

    @Override
    public List<Origen> ListarTodo() {
        return origenRepository.findAll();
    }

    @Override
    public Optional<Origen> busrcarPorId(Long id) {
        return origenRepository.findById(id);
    }

    @Override
    public Origen actualizar(Long id, Origen origen) {
        Origen exitente = origenRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Origen no encontado"));
        exitente.setNombre(origen.getNombre());
        exitente.setDescripcion(origen.getDescripcion());
        exitente.setTipoOrigen(origen.getTipoOrigen());
        return origenRepository.save(exitente);
    }

    @Override
    public void eliminar(Long id) {
        origenRepository.deleteById(id);

    }
}
