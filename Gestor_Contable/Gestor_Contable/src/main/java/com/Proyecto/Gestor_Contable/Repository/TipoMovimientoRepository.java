package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;


@Repository
public interface TipoMovimientoRepository extends MongoRepository<TipoMovimiento, String> {

}

