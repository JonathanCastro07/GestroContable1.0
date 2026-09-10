package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.PagoPeriodico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoPeriodicoRepository extends MongoRepository<PagoPeriodico, String> {
    List<PagoPeriodico> findByNegocioId(String negocioId);
    List<PagoPeriodico> findByNegocioIdAndActivoTrue(String negocioId);
    List<PagoPeriodico> findByNegocioIdAndActivoTrueAndFechaPagoBetween(String negocioId, LocalDate desde, LocalDate hasta);
}