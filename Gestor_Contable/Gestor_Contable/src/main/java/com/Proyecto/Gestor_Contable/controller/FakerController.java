package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.modelo.*;
import com.Proyecto.Gestor_Contable.repository.*;
import com.Proyecto.Gestor_Contable.dtos.LoginRequest;
import com.Proyecto.Gestor_Contable.service.UsuarioService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@RestController
public class FakerController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NegocioRepository negocioRepository;
    @Autowired
    private MovimientoRepository movimientoRepository;
    @Autowired
    private TipoMovimientoRepository tipoMovimientoRepository;
    @Autowired
    private OrigenRepository origenRepository;
    @Autowired
    private PeriodoRepository periodoRepository;

    private final Random random = new Random();

    @PostMapping("/api/test/generar-usuarios")
    public ResponseEntity<String> generarUsuarios(@RequestParam(defaultValue = "1000") int cantidad){
        Faker faker = new Faker();
        String passwordCifrada = passwordEncoder.encode("Prueba123");
        List<Usuario> usuarios = new ArrayList<>();

        for (int i = 0; i < cantidad; i++){
            Usuario usuario = new Usuario();
            usuario.setNombre(faker.name().fullName());
            usuario.setCorreo("usuario" + i + "_" + System.currentTimeMillis() + "@prueba.com");
            usuario.setPassword(passwordCifrada);
            usuario.setTelefono(faker.phoneNumber().phoneNumber());
            usuario.setPreguntaSeguridad("¿Cuál es tu color favorito?");
            usuario.setRespuestaSeguridad("Azul");
            usuarios.add(usuario);
        }

        usuarioRepository.saveAll(usuarios);
        return ResponseEntity.ok(cantidad + " usuarios generados");
    }

    @PostMapping("/api/test/simular-logins")
    public ResponseEntity<String> simularLogins(@RequestParam(defaultValue = "1000") int cantidad){
        List<Usuario> usuarios = usuarioRepository.findAll();
        int totalIntentados = Math.min(cantidad, usuarios.size());
        int exitosos = 0;

        for (int i = 0; i < totalIntentados; i++){
            String correo = usuarios.get(i).getCorreo();
            try {
                LoginRequest request = new LoginRequest(correo, "Prueba123");
                usuarioService.iniciaSesion(request);
                exitosos++;
            } catch (Exception e){

            }
        }

        return ResponseEntity.ok(
                "Intentados: " + totalIntentados + " | Exitosos: " + exitosos
        );
    }

    @PostMapping("/api/test/generar-negocios")
    public ResponseEntity<String> generarNegocios(@RequestParam(defaultValue = "1000") int cantidad){
        Faker faker = new Faker();
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()){
            return ResponseEntity.badRequest().body("No hay usuarios. Corre /api/test/generar-usuarios primero.");
        }

        String[] tipos = {"COMERCIO", "SERVICIOS", "INDUSTRIA", "AGRICULTURA", "TECNOLOGIA"};
        List<Negocio> negocios = new ArrayList<>();

        for (int i = 0; i < cantidad; i++){
            Usuario usuarioAleatorio = usuarios.get(random.nextInt(usuarios.size()));

            Negocio negocio = new Negocio();
            negocio.setNombreNegocio(faker.company().name());
            negocio.setTipoActividad(tipos[random.nextInt(tipos.length)]);
            negocio.setCapitalInicial(100 + random.nextDouble() * 50000);
            negocio.setUsuarioId(usuarioAleatorio.getIdUsuario());
            negocios.add(negocio);
        }

        negocioRepository.saveAll(negocios);
        return ResponseEntity.ok(cantidad + " negocios generados");
    }

    @PostMapping("/api/test/generar-movimientos")
    public ResponseEntity<String> generarMovimientos(@RequestParam(defaultValue = "1000") int cantidad){
        Faker faker = new Faker();
        List<Negocio> negocios = negocioRepository.findAll();
        if (negocios.isEmpty()){
            return ResponseEntity.badRequest().body("No hay negocios. Corre /api/test/generar-negocios primero.");
        }

        asegurarDatosBase();
        List<TipoMovimiento> tipos = tipoMovimientoRepository.findAll();
        List<Origen> origenes = origenRepository.findAll();
        List<Periodo> periodos = periodoRepository.findAll();

        List<MovimientoFinanciero> movimientos = new ArrayList<>();

        for (int i = 0; i < cantidad; i++){
            Negocio negocioAleatorio = negocios.get(random.nextInt(negocios.size()));

            MovimientoFinanciero movimiento = new MovimientoFinanciero();
            movimiento.setMonto(10 + random.nextDouble() * 5000);
            movimiento.setFecha(LocalDateTime.now().minusDays(random.nextInt(365)));
            movimiento.setDescripcion(faker.commerce().productName());
            movimiento.setNegocioId(negocioAleatorio.getIdNegocio());
            movimiento.setTipoMovimientoId(tipos.get(random.nextInt(tipos.size())).getIdTipoMovimiento());
            movimiento.setOrigenId(origenes.get(random.nextInt(origenes.size())).getIdOrigen());
            movimiento.setPeriodoId(periodos.get(random.nextInt(periodos.size())).getIdPeriodo());
            movimientos.add(movimiento);
        }

        movimientoRepository.saveAll(movimientos);
        return ResponseEntity.ok(cantidad + " movimientos generados");
    }

    private void asegurarDatosBase(){
        if (tipoMovimientoRepository.count() == 0){
            tipoMovimientoRepository.save(new TipoMovimiento(null, "INGRESO", NaturalezaMovimiento.CREDITO));
            tipoMovimientoRepository.save(new TipoMovimiento(null, "EGRESO", NaturalezaMovimiento.DEBITO));
            tipoMovimientoRepository.save(new TipoMovimiento(null, "GASTO", NaturalezaMovimiento.DEBITO));
        }
        if (origenRepository.count() == 0){
            origenRepository.save(new Origen(null, "Venta directa", "Ingreso por venta", TipoOrigen.CLIENTE));
            origenRepository.save(new Origen(null, "Pago proveedor", "Pago a proveedor", TipoOrigen.PROVEEDOR));
        }
        if (periodoRepository.count() == 0){
            String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                    "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
            for (String mes : meses){
                periodoRepository.save(new Periodo(null, mes, 2026));
            }
        }
    }


    @PostMapping("/api/test/generar-dataset")
    public ResponseEntity<String> generarDataset(
            @RequestParam(defaultValue = "4000") int cantidad,
            @RequestParam(defaultValue = "true") boolean limpiar) {

        List<Negocio> negocios = negocioRepository.findAll();
        if (negocios.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay negocios. Genera negocios primero.");
        }

        Faker faker = new Faker();
        Random rnd = new Random();

        if (limpiar) movimientoRepository.deleteAll();

        // ---- Catálogos (se crean si no existen) ----
        Map<String, TipoMovimiento> tipos = new HashMap<>();
        tipoMovimientoRepository.findAll().forEach(t -> tipos.put(t.getNombre(), t));
        TipoMovimiento ingreso = tipo(tipos, "INGRESO", NaturalezaMovimiento.CREDITO);
        TipoMovimiento egreso  = tipo(tipos, "EGRESO",  NaturalezaMovimiento.DEBITO);
        TipoMovimiento gasto   = tipo(tipos, "GASTO",   NaturalezaMovimiento.DEBITO);

        Map<String, Origen> origenes = new HashMap<>();
        origenRepository.findAll().forEach(o -> origenes.put(o.getNombre(), o));
        List<Origen> origenesIngreso = List.of(
                origen(origenes, "Venta directa", "Ingreso por venta en local", TipoOrigen.CLIENTE),
                origen(origenes, "Venta online", "Ingreso por venta en línea", TipoOrigen.CLIENTE),
                origen(origenes, "Servicios prestados", "Ingreso por servicios", TipoOrigen.CLIENTE));
        List<Origen> origenesEgreso = List.of(
                origen(origenes, "Pago proveedor", "Pago a proveedor", TipoOrigen.PROVEEDOR),
                origen(origenes, "Compra de insumos", "Compra de materia prima", TipoOrigen.PROVEEDOR));

        Map<String, Periodo> periodos = new HashMap<>();
        periodoRepository.findAll().forEach(p -> periodos.put(p.getMes() + "-" + p.getAnio(), p));

        // ---- Pesos: unos negocios venden mucho más que otros ----
        double[] pesosNegocio = new double[negocios.size()];
        for (int i = 0; i < pesosNegocio.length; i++) {
            pesosNegocio[i] = 0.3 + rnd.nextDouble() * rnd.nextDouble() * 4;
        }

        // ---- Últimos 12 meses con estacionalidad (índice 0 = enero) ----
        double[] estacionalidad = {0.7, 0.7, 0.9, 0.95, 1.0, 1.0, 1.05, 1.0, 1.0, 1.1, 1.3, 1.6};
        List<YearMonth> meses = new ArrayList<>();
        double[] pesosMes = new double[12];
        for (int i = 0; i < 12; i++) {
            YearMonth ym = YearMonth.now().minusMonths(11 - i);
            meses.add(ym);
            pesosMes[i] = estacionalidad[ym.getMonthValue() - 1];
        }

        String[] descGastos = {"Arriendo", "Servicios públicos", "Nómina", "Publicidad",
                "Mantenimiento", "Transporte", "Internet y telefonía"};

        List<MovimientoFinanciero> movimientos = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            int idxNegocio = elegirPonderado(pesosNegocio, rnd);
            Negocio negocio = negocios.get(idxNegocio);
            double pesoNegocio = pesosNegocio[idxNegocio];

            // Fecha dentro de un mes elegido según estacionalidad (sin fechas futuras)
            YearMonth ym = meses.get(elegirPonderado(pesosMes, rnd));
            int maxDia = ym.equals(YearMonth.now()) ? LocalDate.now().getDayOfMonth() : ym.lengthOfMonth();
            LocalDateTime fecha = ym.atDay(1 + rnd.nextInt(maxDia))
                    .atTime(8 + rnd.nextInt(10), rnd.nextInt(60));


            double p = rnd.nextDouble();
            TipoMovimiento tipo;
            Origen origen;
            double monto;
            String descripcion;

            if (p < 0.60) {
                tipo = ingreso;
                origen = origenesIngreso.get(rnd.nextInt(origenesIngreso.size()));
                monto = (150 + rnd.nextDouble() * 1850) * pesoNegocio;
                descripcion = "Venta de " + faker.commerce().productName();
            } else if (p < 0.85) {                // GASTO
                tipo = gasto;
                origen = origenesEgreso.get(rnd.nextInt(origenesEgreso.size()));
                monto = (10 + rnd.nextDouble() * 190) * (0.5 + pesoNegocio / 2);
                descripcion = descGastos[rnd.nextInt(descGastos.length)];
            } else {
                tipo = egreso;
                origen = origenesEgreso.get(rnd.nextInt(origenesEgreso.size()));
                monto = (500 + rnd.nextDouble() * 3000) * pesoNegocio * 0.8;
                descripcion = "Pago a " + faker.company().name();
            }

            String mes = MESES[fecha.getMonthValue() - 1];
            Periodo periodo = periodos.computeIfAbsent(mes + "-" + fecha.getYear(),
                    k -> periodoRepository.save(new Periodo(null, mes, fecha.getYear())));

            MovimientoFinanciero m = new MovimientoFinanciero();
            m.setMonto(Math.round(monto * 100.0) / 100.0);
            m.setFecha(fecha);
            m.setDescripcion(descripcion);
            m.setNegocioId(negocio.getIdNegocio());
            m.setTipoMovimientoId(tipo.getIdTipoMovimiento());
            m.setOrigenId(origen.getIdOrigen());
            m.setPeriodoId(periodo.getIdPeriodo());
            movimientos.add(m);
        }

        movimientoRepository.saveAll(movimientos);
        return ResponseEntity.ok(cantidad + " movimientos generados en " + negocios.size() + " negocios");
    }

    // ---------- Helpers ----------
    private static final String[] MESES = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};

    private TipoMovimiento tipo(Map<String, TipoMovimiento> cache, String nombre, NaturalezaMovimiento nat) {
        return cache.computeIfAbsent(nombre,
                k -> tipoMovimientoRepository.save(new TipoMovimiento(null, k, nat)));
    }

    private Origen origen(Map<String, Origen> cache, String nombre, String desc, TipoOrigen tipo) {
        return cache.computeIfAbsent(nombre,
                k -> origenRepository.save(new Origen(null, k, desc, tipo)));
    }

    private int elegirPonderado(double[] pesos, Random rnd) {
        double total = Arrays.stream(pesos).sum();
        double r = rnd.nextDouble() * total;
        double acum = 0;
        for (int i = 0; i < pesos.length; i++) {
            acum += pesos[i];
            if (r <= acum) return i;
        }
        return pesos.length - 1;
    }
}