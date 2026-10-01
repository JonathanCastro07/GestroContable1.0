package com.Proyecto.Gestor_Contable.repository.sql;

import com.Proyecto.Gestor_Contable.modelo.sql.PagoPeriodicoSql;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoPeriodicoSqlRepository extends JpaRepository<PagoPeriodicoSql, String> {
}