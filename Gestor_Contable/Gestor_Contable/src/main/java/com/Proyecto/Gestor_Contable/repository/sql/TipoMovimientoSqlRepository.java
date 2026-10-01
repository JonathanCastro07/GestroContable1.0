package com.Proyecto.Gestor_Contable.repository.sql;

import com.Proyecto.Gestor_Contable.modelo.sql.TipoMovimientoSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMovimientoSqlRepository extends JpaRepository<TipoMovimientoSql, String> {
}