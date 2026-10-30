package com.Proyecto.Gestor_Contable.repository;

import com.Proyecto.Gestor_Contable.modelo.Negocio;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;

@Repository
public interface NegocioRepository extends MongoRepository<Negocio, String> {
    List<Negocio> findByUsuario_Correo(String correo);
    Optional<Negocio> findFirstByUsuario_Correo(String correo);
    boolean existsByUsuario_Correo(String correo);
}
