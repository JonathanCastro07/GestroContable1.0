
package com.Proyecto.Gestor_Contable.Repository;



import com.Proyecto.Gestor_Contable.Modelo.PagoPeriodico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoPeriodicoRepository extends JpaRepository<PagoPeriodico, Long> {
    List<PagoPeriodico> findByNegocioIdNegocio(Long idNegocio);
    List<PagoPeriodico> findByNegocioIdNegocioAndActivoTrue(Long idNegocio);
    List<PagoPeriodico> findByNegocioIdNegocioAndActivoTrueAndFechaPagoBetween(
            Long idNegocio, LocalDate desde, LocalDate hasta);

    @Query("SELECT p FROM PagoPeriodico p " +
            "WHERE p.negocio.idNegocio = :idNegocio " +
            "AND MONTH(p.fechaPago) = :mes " +
            "AND YEAR(p.fechaPago) = :anio")
    List<PagoPeriodico> findByNegocioAndPeriodo(
            @Param("idNegocio") Long idNegocio,
            @Param("mes") Integer mes,
            @Param("anio") Integer anio);
}
