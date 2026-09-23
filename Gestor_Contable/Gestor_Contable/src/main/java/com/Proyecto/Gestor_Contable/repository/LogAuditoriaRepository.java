package com.Proyecto.Gestor_Contable.repository;

import com.Proyecto.Gestor_Contable.modelo.LogAuditoria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LogAuditoriaRepository extends MongoRepository<LogAuditoria, String> {

    List<LogAuditoria> findByUsuarioId(String usuarioId);
    List<LogAuditoria> findByAccion(String accion);
}
