package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.Negocio;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

@Repository
public interface NegocioRepository extends MongoRepository<Negocio, String> {
    List<Negocio> findByUsuarioId(String usuarioId);
}
