package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.Modelo.Periodo;
import com.Proyecto.Gestor_Contable.Repository.PeriodoRepository;
import com.Proyecto.Gestor_Contable.Service.PeriodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class PeriodoServiceImpl implements PeriodoService {
    @Autowired
    private PeriodoRepository periodoRepository;
    @Override
    public Periodo crear(Periodo periodo) {
        return periodoRepository.save(periodo);
    }

    @Override
    public List<Periodo> listarTodo() {
        return periodoRepository.findAll();
    }

    @Override
    public Optional<Periodo> BuscarPorId(Long id) {
        return periodoRepository.findById(id);
    }

    @Override
    public Periodo actualizar(Long id, Periodo periodo) {
        Periodo existente = periodoRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Periodo no encontado"));
        existente.setMes(periodo.getMes());
        existente.setAnio(periodo.getAnio());
        return periodoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        periodoRepository.deleteById(id);
    }
}
