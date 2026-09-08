package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.MovimientoFinanciero;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoRepository extends MongoRepository<MovimientoFinanciero, String> {
    List<MovimientoFinanciero> findByNegocioId(String negocioId);
    List<MovimientoFinanciero> findByNegocioIdAndFechaBetween(String negocioId, LocalDate desde, LocalDate hasta);

    List<MovimientoFinanciero> findByNegocioIdAndTipoMovimientoId(String negocioId, String tipoMovimientoId);

    List<MovimientoFinanciero> findByNegocioIdAndPeriodoId(String negocioId, String periodoId);
}
