package com.Proyecto.Gestor_Contable.repository;

import com.Proyecto.Gestor_Contable.modelo.Periodo;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface PeriodoRepository extends MongoRepository<Periodo,String> {

    Optional<Periodo> findByMesAndAnio(String mes, Integer anio);
}
