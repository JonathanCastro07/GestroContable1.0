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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NegocioRepository negocioRepository;
    @Autowired
    private MovimientoRepository movimientoRepository;

    private final Random random = new Random();

    private static final String[] TIPOS_ACTIVIDAD = {"COMERCIO", "SERVICIOS", "INDUSTRIA", "AGRICULTURA", "TECNOLOGIA"};
    private static final String[] ORIGENES_INGRESO = {"Venta directa", "Venta online", "Servicios prestados"};
    private static final String[] ORIGENES_EGRESO = {"Pago proveedor", "Compra de insumos"};

    @PostMapping("/api/test/generar-usuarios")
    public ResponseEntity<String> generarUsuarios(@RequestParam(defaultValue = "1000") int cantidad){
        Faker faker = new Faker();
        String passwordCifrada = passwordEncoder.encode("Prueba123");
        List<Negocio> negocios = new ArrayList<>();

        for (int i = 0; i < cantidad; i++){
            Usuario usuario = new Usuario();
            usuario.setNombre(faker.name().fullName());
            usuario.setCorreo("usuario" + i + "_" + System.currentTimeMillis() + "@prueba.com");
            usuario.setPassword(passwordCifrada);
            usuario.setTelefono(faker.phoneNumber().phoneNumber());
            usuario.setPreguntaSeguridad("¿Cuál es tu color favorito?");
            usuario.setRespuestaSeguridad("Azul");

            Negocio negocio = new Negocio();
            negocio.setNombreNegocio(faker.company().name());
            negocio.setTipoActividad(TIPOS_ACTIVIDAD[random.nextInt(TIPOS_ACTIVIDAD.length)]);
            negocio.setCapitalInicial(100 + random.nextDouble() * 50000);
            negocio.setRolPropietario("PROPIETARIO");
            negocio.setUsuario(usuario);
            negocios.add(negocio);
        }

        negocioRepository.saveAll(negocios);
        return ResponseEntity.ok(cantidad + " usuarios (con su negocio) generados");
    }

    @PostMapping("/api/test/simular-logins")
    public ResponseEntity<String> simularLogins(@RequestParam(defaultValue = "1000") int cantidad){
        List<Negocio> negocios = negocioRepository.findAll();
        int totalIntentados = Math.min(cantidad, negocios.size());
        int exitosos = 0;

        for (int i = 0; i < totalIntentados; i++){
            String correo = negocios.get(i).getUsuario().getCorreo();
            try {
                usuarioService.iniciaSesion(new LoginRequest(correo, "Prueba123"));
                exitosos++;
            } catch (Exception e){
            }
        }

        return ResponseEntity.ok("Intentados: " + totalIntentados + " | Exitosos: " + exitosos);
    }

    @PostMapping("/api/test/generar-negocios")
    public ResponseEntity<String> generarNegocios(@RequestParam(defaultValue = "1000") int cantidad){
        Faker faker = new Faker();
        List<Negocio> existentes = negocioRepository.findAll();
        if (existentes.isEmpty()){
            return ResponseEntity.badRequest().body("No hay usuarios. Corre /api/test/generar-usuarios primero.");
        }

        List<Negocio> negocios = new ArrayList<>();
        for (int i = 0; i < cantidad; i++){
            Usuario usuarioAleatorio = existentes.get(random.nextInt(existentes.size())).getUsuario();

            Negocio negocio = new Negocio();
            negocio.setNombreNegocio(faker.company().name());
            negocio.setTipoActividad(TIPOS_ACTIVIDAD[random.nextInt(TIPOS_ACTIVIDAD.length)]);
            negocio.setCapitalInicial(100 + random.nextDouble() * 50000);
            negocio.setRolPropietario("PROPIETARIO");
            negocio.setUsuario(usuarioAleatorio);
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

        List<MovimientoFinanciero> movimientos = new ArrayList<>();

        for (int i = 0; i < cantidad; i++){
            Negocio negocioAleatorio = negocios.get(random.nextInt(negocios.size()));
            LocalDateTime fecha = LocalDateTime.now().minusDays(random.nextInt(365));

            TipoMovimiento tipo;
            Origen origen;
            switch (random.nextInt(3)) {
                case 0 -> {
                    tipo = TipoMovimiento.nuevo("INGRESO", NaturalezaMovimiento.CREDITO);
                    origen = Origen.nuevo(ORIGENES_INGRESO[random.nextInt(ORIGENES_INGRESO.length)], TipoOrigen.CLIENTE);
                }
                case 1 -> {
                    tipo = TipoMovimiento.nuevo("EGRESO", NaturalezaMovimiento.DEBITO);
                    origen = Origen.nuevo(ORIGENES_EGRESO[random.nextInt(ORIGENES_EGRESO.length)], TipoOrigen.PROVEEDOR);
                }
                default -> {
                    tipo = TipoMovimiento.nuevo("GASTO", NaturalezaMovimiento.DEBITO);
                    origen = Origen.nuevo(ORIGENES_EGRESO[random.nextInt(ORIGENES_EGRESO.length)], TipoOrigen.PROVEEDOR);
                }
            }

            MovimientoFinanciero movimiento = new MovimientoFinanciero();
            movimiento.setMonto(10 + random.nextDouble() * 5000);
            movimiento.setFecha(fecha);
            movimiento.setDescripcion(faker.commerce().productName());
            movimiento.setNegocioId(negocioAleatorio.getIdNegocio());
            movimiento.setTipoMovimiento(tipo);
            movimiento.setOrigen(origen);
            movimiento.setPeriodo(Periodo.de(fecha));
            movimientos.add(movimiento);
        }

        movimientoRepository.saveAll(movimientos);
        return ResponseEntity.ok(cantidad + " movimientos generados");
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
                tipo = TipoMovimiento.nuevo("INGRESO", NaturalezaMovimiento.CREDITO);
                origen = Origen.nuevo(ORIGENES_INGRESO[rnd.nextInt(ORIGENES_INGRESO.length)], TipoOrigen.CLIENTE);
                monto = (150 + rnd.nextDouble() * 1850) * pesoNegocio;
                descripcion = "Venta de " + faker.commerce().productName();
            } else if (p < 0.85) {
                tipo = TipoMovimiento.nuevo("GASTO", NaturalezaMovimiento.DEBITO);
                origen = Origen.nuevo(ORIGENES_EGRESO[rnd.nextInt(ORIGENES_EGRESO.length)], TipoOrigen.PROVEEDOR);
                monto = (10 + rnd.nextDouble() * 190) * (0.5 + pesoNegocio / 2);
                descripcion = descGastos[rnd.nextInt(descGastos.length)];
            } else {
                tipo = TipoMovimiento.nuevo("EGRESO", NaturalezaMovimiento.DEBITO);
                origen = Origen.nuevo(ORIGENES_EGRESO[rnd.nextInt(ORIGENES_EGRESO.length)], TipoOrigen.PROVEEDOR);
                monto = (500 + rnd.nextDouble() * 3000) * pesoNegocio * 0.8;
                descripcion = "Pago a " + faker.company().name();
            }

            MovimientoFinanciero m = new MovimientoFinanciero();
            m.setMonto(Math.round(monto * 100.0) / 100.0);
            m.setFecha(fecha);
            m.setDescripcion(descripcion);
            m.setNegocioId(negocio.getIdNegocio());
            m.setTipoMovimiento(tipo);
            m.setOrigen(origen);
            m.setPeriodo(Periodo.de(fecha));
            movimientos.add(m);
        }

        movimientoRepository.saveAll(movimientos);
        return ResponseEntity.ok(cantidad + " movimientos generados en " + negocios.size() + " negocios");
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