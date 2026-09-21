package com.Proyecto.Gestor_Contable.repository;

import com.Proyecto.Gestor_Contable.modelo.TipoMovimiento;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;


@Repository
public interface TipoMovimientoRepository extends MongoRepository<TipoMovimiento, String> {

}

