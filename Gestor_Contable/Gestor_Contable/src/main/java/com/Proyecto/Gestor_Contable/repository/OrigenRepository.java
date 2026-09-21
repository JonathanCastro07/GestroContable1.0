package com.Proyecto.Gestor_Contable.repository;

import com.Proyecto.Gestor_Contable.modelo.Origen;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrigenRepository extends MongoRepository<Origen, String> {
}
