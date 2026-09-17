package com.Proyecto.Gestor_Contable.repository;

import com.Proyecto.Gestor_Contable.modelo.AnalisisFinancieroIA;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AnalisisFinancieroIARepository extends MongoRepository<AnalisisFinancieroIA, String> {
    List<AnalisisFinancieroIA> findbyNegocioId(String negocioid);
}
