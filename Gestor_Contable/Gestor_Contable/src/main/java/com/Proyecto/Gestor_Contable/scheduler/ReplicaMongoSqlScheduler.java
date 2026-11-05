package com.Proyecto.Gestor_Contable.scheduler;

import com.Proyecto.Gestor_Contable.modelo.*;
import com.Proyecto.Gestor_Contable.modelo.sql.*;
import com.Proyecto.Gestor_Contable.repository.*;
import com.Proyecto.Gestor_Contable.repository.sql.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReplicaMongoSqlScheduler {

    @Autowired
    private  OrigenSqlRepository origenSqlRepository;
    @Autowired
    private  TipoMovimientoSqlRepository tipoMovimientoSqlRepository;
    @Autowired
    private  PeriodoSqlRepository periodoSqlRepository;
    @Autowired
    private  PagoPeriodicoRepository pagoPeriodicoRepository;
    @Autowired
    private  PagoPeriodicoSqlRepository pagoPeriodicoSqlRepository;
    @Autowired
    private  NegocioRepository negocioRepository;
    @Autowired
    private  NegocioSqlRepository negocioSqlRepository;
    @Autowired
    private  MovimientoRepository movimientoRepository;
    @Autowired
    private  MovimientoFinancieroSqlRepository movimientoSqlRepository;
    @Autowired
    private  UsuarioSqlRepository usuarioSqlRepository;

    @Scheduled(fixedRate = 360000)
    public void replicarTodo() {
        replicarCatalogos();
        replicarPagoPeriodico();
        replicarNegocio();
        replicarMovimiento();
        replicarUsuario();
    }

    private void replicarCatalogos() {
        Set<String> origenes = new HashSet<>();
        Set<String> tipos = new HashSet<>();
        Set<String> periodos = new HashSet<>();

        movimientoRepository.findAll().forEach(m -> {
            guardarOrigen(m.getOrigen(), origenes);
            guardarTipo(m.getTipoMovimiento(), tipos);
            guardarPeriodo(m.getPeriodo(), periodos);
        });
        pagoPeriodicoRepository.findAll().forEach(p -> {
            guardarOrigen(p.getOrigen(), origenes);
            guardarTipo(p.getTipoMovimiento(), tipos);
        });
    }

    private void guardarOrigen(Origen o, Set<String> yaGuardados) {
        if (o == null || o.getIdOrigen() == null || !yaGuardados.add(o.getIdOrigen())) return;
        OrigenSql sql = new OrigenSql();
        sql.setIdOrigen(o.getIdOrigen());
        sql.setNombre(o.getNombre());
        sql.setTipoOrigen(o.getTipoOrigen());
        origenSqlRepository.save(sql);
    }

    private void guardarTipo(TipoMovimiento t, Set<String> yaGuardados) {
        if (t == null || t.getIdTipoMovimiento() == null || !yaGuardados.add(t.getIdTipoMovimiento())) return;
        TipoMovimientoSql sql = new TipoMovimientoSql();
        sql.setIdTipoMovimiento(t.getIdTipoMovimiento());
        sql.setNombre(t.getNombre());
        sql.setNaturaleza(t.getNaturaleza());
        tipoMovimientoSqlRepository.save(sql);
    }

    private void guardarPeriodo(Periodo p, Set<String> yaGuardados) {
        if (p == null || p.getIdPeriodo() == null || !yaGuardados.add(p.getIdPeriodo())) return;
        PeriodoSql sql = new PeriodoSql();
        sql.setIdPeriodo(p.getIdPeriodo());
        sql.setMes(p.getMes());
        sql.setAnio(p.getAnio());
        periodoSqlRepository.save(sql);
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
            sql.setTipoMovimientoId(p.getTipoMovimiento() != null ? p.getTipoMovimiento().getIdTipoMovimiento() : null);
            sql.setOrigenId(p.getOrigen() != null ? p.getOrigen().getIdOrigen() : null);
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
            sql.setUsuarioId(n.getUsuario() != null ? n.getUsuario().getCorreo() : null);
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
            sql.setTipoMovimientoId(m.getTipoMovimiento() != null ? m.getTipoMovimiento().getIdTipoMovimiento() : null);
            sql.setOrigenId(m.getOrigen() != null ? m.getOrigen().getIdOrigen() : null);
            sql.setPeriodoId(m.getPeriodo() != null ? m.getPeriodo().getIdPeriodo() : null);
            movimientoSqlRepository.save(sql);
        });
    }

    private void replicarUsuario() {
        Set<String> yaReplicados = new HashSet<>();
        negocioRepository.findAll().forEach(n -> {
            Usuario u = n.getUsuario();
            if (u == null || u.getCorreo() == null || !yaReplicados.add(u.getCorreo())) return;
            UsuarioSql sql = new UsuarioSql();
            sql.setIdUsuario(u.getCorreo());
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