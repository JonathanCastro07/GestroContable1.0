package com.Proyecto.Gestor_Contable.Repository;

import com.Proyecto.Gestor_Contable.Modelo.MovimientoFinanciero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoFinanciero,Long> {
    List<MovimientoFinanciero> findByNegocioIdNegocio(Long idNegocio);
    List<MovimientoFinanciero> findByNegocioIdNegocioAndFechaBetween(Long idNegocio, LocalDate desde, LocalDate hasta);
    @Query("SELECT SUM(m.monto) FROM MovimientoFinanciero m " +
            "WHERE m.negocio.idNegocio = :idNegocio AND m.tipoMovimiento.nombre = :tipo")
    Double sumMontoByNegocioAndTipo(@Param("idNegocio") Long idNegocio,
                                    @Param("tipo") String tipo);

    @Query("SELECT m FROM MovimientoFinanciero m " +
            "WHERE m.negocio.idNegocio = :idNegocio " +
            "AND m.periodo.mes = :mes " +
            "AND m.periodo.anio = :anio")
    List<MovimientoFinanciero> findByNegocioAndPeriodo(
            @Param("idNegocio") Long idNegocio,
            @Param("mes") String mes,
            @Param("anio") Integer anio);
}
