package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoRepository extends JpaRepository<Periodo,Long> {

    Optional<Periodo> findByMesAndAnio(String mes, Integer anio);
}
