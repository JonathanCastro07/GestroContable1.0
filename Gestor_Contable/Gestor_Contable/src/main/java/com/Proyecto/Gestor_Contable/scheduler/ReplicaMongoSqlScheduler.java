package com.Proyecto.Gestor_Contable.scheduler;

import com.Proyecto.Gestor_Contable.modelo.*;
import com.Proyecto.Gestor_Contable.modelo.sql.*;
import com.Proyecto.Gestor_Contable.repository.*;
import com.Proyecto.Gestor_Contable.repository.sql.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplicaMongoSqlScheduler {

    private final OrigenRepository origenRepository;
    private final OrigenSqlRepository origenSqlRepository;

    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final TipoMovimientoSqlRepository tipoMovimientoSqlRepository;

    private final PeriodoRepository periodoRepository;
    private final PeriodoSqlRepository periodoSqlRepository;

    private final PagoPeriodicoRepository pagoPeriodicoRepository;
    private final PagoPeriodicoSqlRepository pagoPeriodicoSqlRepository;

    private final NegocioRepository negocioRepository;
    private final NegocioSqlRepository negocioSqlRepository;

    private final MovimientoRepository movimientoRepository;
    private final MovimientoFinancieroSqlRepository movimientoSqlRepository;

    private final UsuarioRepository usuarioRepository;
    private final UsuarioSqlRepository usuarioSqlRepository;

    @Scheduled(fixedRate = 3600000)
    public void replicarTodo() {
        replicarOrigen();
        replicarTipoMovimiento();
        replicarPeriodo();
        replicarPagoPeriodico();
        replicarNegocio();
        replicarMovimiento();
        replicarUsuario();
    }

    private void replicarOrigen() {
        origenRepository.findAll().forEach(o -> {
            OrigenSql sql = new OrigenSql();
            sql.setIdOrigen(o.getIdOrigen());
            sql.setNombre(o.getNombre());
            sql.setDescripcion(o.getDescripcion());
            sql.setTipoOrigen(o.getTipoOrigen());
            origenSqlRepository.save(sql);
        });
    }

    private void replicarTipoMovimiento() {
        tipoMovimientoRepository.findAll().forEach(t -> {
            TipoMovimientoSql sql = new TipoMovimientoSql();
            sql.setIdTipoMovimiento(t.getIdTipoMovimiento());
            sql.setNombre(t.getNombre());
            sql.setNaturaleza(t.getNaturaleza());
            tipoMovimientoSqlRepository.save(sql);
        });
    }

    private void replicarPeriodo() {
        periodoRepository.findAll().forEach(p -> {
            PeriodoSql sql = new PeriodoSql();
            sql.setIdPeriodo(p.getIdPeriodo());
            sql.setMes(p.getMes());
            sql.setAnio(p.getAnio());
            periodoSqlRepository.save(sql);
        });
    }

    private void replicarPagoPeriodico() {
        pagoPeriodicoRepository.findAll().forEach(p -> {
            PagoPeriodicoSql sql = new PagoPeriodicoSql();
            sql.setIdPago(p.getIdPago());
            sql.setNombre(p.getNombre());
            sql.setMonto(p.getMonto());
            sql.setFechaPago(p.getFechaPago());
            sql.setActivo(p.isActivo());
            sql.setDescripcion(p.getDescripcion());
            sql.setNegocioId(p.getNegocioId());
            sql.setTipoMovimientoId(p.getTipoMovimientoId());
            sql.setOrigenId(p.getOrigenId());
            pagoPeriodicoSqlRepository.save(sql);
        });
    }

    private void replicarNegocio() {
        negocioRepository.findAll().forEach(n -> {
            NegocioSql sql = new NegocioSql();
            sql.setIdNegocio(n.getIdNegocio());
            sql.setNombreNegocio(n.getNombreNegocio());
            sql.setTipoActividad(n.getTipoActividad());
            sql.setCapitalInicial(n.getCapitalInicial());
            sql.setRolPropietario(n.getRolPropietario());
            sql.setUsuarioId(n.getUsuarioId());
            negocioSqlRepository.save(sql);
        });
    }

    private void replicarMovimiento() {
        movimientoRepository.findAll().forEach(m -> {
            MovimientoFinancieroSql sql = new MovimientoFinancieroSql();
            sql.setIdMovimiento(m.getIdMovimiento());
            sql.setMonto(m.getMonto());
            sql.setFecha(m.getFecha());
            sql.setDescripcion(m.getDescripcion());
            sql.setNegocioId(m.getNegocioId());
            sql.setTipoMovimientoId(m.getTipoMovimientoId());
            sql.setOrigenId(m.getOrigenId());
            sql.setPeriodoId(m.getPeriodoId());
            movimientoSqlRepository.save(sql);
        });
    }

    private void replicarUsuario() {
        usuarioRepository.findAll().forEach(u -> {
            UsuarioSql sql = new UsuarioSql();
            sql.setIdUsuario(u.getIdUsuario());
            sql.setNombre(u.getNombre());
            sql.setCorreo(u.getCorreo());
            sql.setTelefono(u.getTelefono());
            sql.setPassword(u.getPassword());
            sql.setPreguntaSeguridad(u.getPreguntaSeguridad());
            sql.setRespuestaSeguridad(u.getRespuestaSeguridad());
            usuarioSqlRepository.save(sql);
        });
    }
}