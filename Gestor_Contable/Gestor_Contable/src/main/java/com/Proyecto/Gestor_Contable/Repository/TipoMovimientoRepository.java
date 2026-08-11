package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Long> {

}

