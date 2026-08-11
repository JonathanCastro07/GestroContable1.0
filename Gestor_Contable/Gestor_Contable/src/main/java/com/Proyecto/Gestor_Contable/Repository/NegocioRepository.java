package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    List<Negocio> findByUsuarioIdUsuario(Long id);
}
