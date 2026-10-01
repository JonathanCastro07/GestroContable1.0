package com.Proyecto.Gestor_Contable.repository.sql;

import com.Proyecto.Gestor_Contable.modelo.sql.UsuarioSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioSqlRepository extends JpaRepository<UsuarioSql, String> {
}