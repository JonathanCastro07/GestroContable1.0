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
    @Autowired
    private PagoPeriodicoRepository pagoPeriodicoRepository;

    private final Random random = new Random();

    private static final String[] TIPOS_ACTIVIDAD = {"COMERCIO", "SERVICIOS", "INDUSTRIA", "AGRICULTURA", "TECNOLOGIA"};
    private static final String[] ORIGENES_INGRESO = {"Venta directa", "Venta online", "Servicios prestados"};
    private static final String[] ORIGENES_EGRESO = {"Pago proveedor", "Compra de insumos"};

    private static final LocalDate FECHA_INICIO = LocalDate.of(2023, 1, 1);
    private static final LocalDate FECHA_FIN = LocalDate.of(2027, 12, 31);

    private LocalDateTime fechaAleatoria() {
        int dias = (int) java.time.temporal.ChronoUnit.DAYS.between(FECHA_INICIO, FECHA_FIN);
        return FECHA_INICIO.plusDays(random.nextInt(dias + 1))
                .atTime(8 + random.nextInt(10), random.nextInt(60));
    }

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
            LocalDateTime fecha = fechaAleatoria();

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

        double[] pesosNegocio = new double[negocios.size()];
        for (int i = 0; i < pesosNegocio.length; i++) {
            pesosNegocio[i] = 0.3 + rnd.nextDouble() * rnd.nextDouble() * 4;
        }

        double[] estacionalidad = {0.7, 0.7, 0.9, 0.95, 1.0, 1.0, 1.05, 1.0, 1.0, 1.1, 1.3, 1.6};
        List<YearMonth> meses = new ArrayList<>();
        double[] pesosMes = new double[60];
        for (int i = 0; i < 60; i++) {
            YearMonth ym = YearMonth.of(2023, 1).plusMonths(i);
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
            int maxDia = ym.lengthOfMonth();
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

    // ============ POBLACIÓN: usuarios + negocios + movimientos básicos y variados ============

    private static final String[] SERVICIOS = {"Energía", "Acueducto", "Internet", "Gas"};
    private static final String[] DESC_GASTOS = {"Arriendo", "Servicios públicos", "Nómina", "Publicidad",
            "Mantenimiento", "Transporte", "Internet y telefonía"};
    private static final int PERFILES = 5;

    @PostMapping("/api/test/generar-poblacion")
    public ResponseEntity<String> generarPoblacion(
            @RequestParam(defaultValue = "100") int usuarios,
            @RequestParam(defaultValue = "20") int variados,
            @RequestParam(defaultValue = "40") int movimientosBasicos) {

        if (variados > usuarios) {
            return ResponseEntity.badRequest().body("'variados' no puede ser mayor que 'usuarios'");
        }

        Faker faker = new Faker();
        String passwordCifrada = passwordEncoder.encode("Prueba123");
        long stamp = System.currentTimeMillis();

        // 1) Usuarios con su negocio
        List<Negocio> nuevos = new ArrayList<>();
        for (int i = 0; i < usuarios; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombre(faker.name().fullName());
            usuario.setCorreo("usuario" + i + "_" + stamp + "@prueba.com");
            usuario.setPassword(passwordCifrada);
            usuario.setTelefono(faker.phoneNumber().phoneNumber());
            usuario.setPreguntaSeguridad("¿Cuál es tu color favorito?");
            usuario.setRespuestaSeguridad("Azul");

            Negocio negocio = new Negocio();
            negocio.setNombreNegocio(faker.company().name());
            negocio.setTipoActividad(TIPOS_ACTIVIDAD[random.nextInt(TIPOS_ACTIVIDAD.length)]);
            negocio.setCapitalInicial(1000 + random.nextDouble() * 50000);
            negocio.setRolPropietario("PROPIETARIO");
            negocio.setUsuario(usuario);
            nuevos.add(negocio);
        }
        List<Negocio> guardados = negocioRepository.saveAll(nuevos);

        // 2) Movimientos: los primeros son básicos, los últimos 'variados' tienen perfiles
        int limite = usuarios - variados;
        List<MovimientoFinanciero> movimientos = new ArrayList<>();
        for (int i = 0; i < guardados.size(); i++) {
            Negocio negocio = guardados.get(i);
            if (i < limite) {
                movimientos.addAll(movimientosBasicos(negocio, movimientosBasicos, faker));
            } else {
                movimientos.addAll(movimientosVariados(negocio, (i - limite) % PERFILES, faker));
            }
        }
        movimientoRepository.saveAll(movimientos);

        return ResponseEntity.ok(usuarios + " usuarios creados (" + limite + " básicos, "
                + variados + " variados). Movimientos generados: " + movimientos.size()
                + ". Contraseña de todos: Prueba123");
    }

    private List<MovimientoFinanciero> movimientosBasicos(Negocio negocio, int cantidad, Faker faker) {
        String[] tipos = {"INGRESO", "EGRESO", "GASTO"};
        List<MovimientoFinanciero> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            LocalDateTime fecha = fechaAleatoria();
            String tipo = tipos[random.nextInt(tipos.length)];
            double monto = 10 + random.nextDouble() * 5000;
            lista.add(armarMovimiento(negocio, fecha, tipo, redondear(monto), faker.commerce().productName(), faker));
        }
        return lista;
    }

    private List<MovimientoFinanciero> movimientosVariados(Negocio negocio, int perfil, Faker faker) {
        int cantidad;
        double pIngreso;
        double pGasto;
        double escala;
        boolean crecimiento = false;
        boolean estacional = false;
        boolean reciente = false;

        switch (perfil) {
            case 0 -> { cantidad = 300; pIngreso = 0.65; pGasto = 0.20; escala = 2.0; }
            case 1 -> { cantidad = 250; pIngreso = 0.30; pGasto = 0.40; escala = 1.0; }
            case 2 -> { cantidad = 350; pIngreso = 0.60; pGasto = 0.25; escala = 1.0; crecimiento = true; }
            case 3 -> { cantidad = 400; pIngreso = 0.55; pGasto = 0.25; escala = 1.5; estacional = true; }
            default -> { cantidad = 12; pIngreso = 0.50; pGasto = 0.30; escala = 0.5; reciente = true; }
        }

        long totalDias = java.time.temporal.ChronoUnit.DAYS.between(FECHA_INICIO, FECHA_FIN);

        List<MovimientoFinanciero> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            LocalDateTime fecha = reciente
                    ? LocalDateTime.now().minusDays(random.nextInt(45))
                    .withHour(8 + random.nextInt(10)).withMinute(random.nextInt(60))
                    : fechaAleatoria();

            double factor = escala;
            if (crecimiento) {
                double progreso = java.time.temporal.ChronoUnit.DAYS.between(FECHA_INICIO, fecha.toLocalDate())
                        / (double) totalDias;
                factor *= 0.4 + 1.6 * progreso;
            }
            if (estacional) {
                int mes = fecha.getMonthValue();
                if (mes == 11 || mes == 12) factor *= 2.2;
                else if (mes == 1 || mes == 2) factor *= 0.6;
            }

            double r = random.nextDouble();
            String tipo;
            double monto;
            String descripcion;
            if (r < pIngreso) {
                tipo = "INGRESO";
                monto = (150 + random.nextDouble() * 1850) * factor;
                descripcion = "Venta de " + faker.commerce().productName();
            } else if (r < pIngreso + pGasto) {
                tipo = "GASTO";
                monto = (10 + random.nextDouble() * 300) * Math.max(factor, 0.8);
                descripcion = DESC_GASTOS[random.nextInt(DESC_GASTOS.length)];
            } else {
                tipo = "EGRESO";
                monto = (300 + random.nextDouble() * 2500) * factor;
                descripcion = "Pago a " + faker.company().name();
            }
            lista.add(armarMovimiento(negocio, fecha, tipo, redondear(monto), descripcion, faker));
        }
        return lista;
    }

    private MovimientoFinanciero armarMovimiento(Negocio negocio, LocalDateTime fecha, String tipoNombre,
                                                 double monto, String descripcion, Faker faker) {
        TipoMovimiento tipo = TipoMovimiento.nuevo(tipoNombre,
                "INGRESO".equals(tipoNombre) ? NaturalezaMovimiento.CREDITO : NaturalezaMovimiento.DEBITO);

        Origen origen;
        if ("INGRESO".equals(tipoNombre)) {
            origen = Origen.nuevo(faker.name().fullName(), TipoOrigen.CLIENTE);
        } else {
            origen = switch (random.nextInt(4)) {
                case 0 -> Origen.nuevo(faker.company().name(), TipoOrigen.PROVEEDOR);
                case 1 -> Origen.nuevo(faker.name().fullName(), TipoOrigen.TRABAJADOR);
                case 2 -> Origen.nuevo(SERVICIOS[random.nextInt(SERVICIOS.length)], TipoOrigen.SERVICIO_PUBLICO);
                default -> Origen.nuevo("Factura " + (1000 + random.nextInt(9000)), TipoOrigen.FACTURA);
            };
        }

        MovimientoFinanciero m = new MovimientoFinanciero();
        m.setMonto(monto);
        m.setFecha(fecha);
        m.setDescripcion(descripcion);
        m.setNegocioId(negocio.getIdNegocio());
        m.setTipoMovimiento(tipo);
        m.setOrigen(origen);
        m.setPeriodo(Periodo.de(fecha));
        return m;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }


    private record PlantillaPago(String nombre, String descripcion, String tipo,
                                 TipoOrigen tipoOrigen, double min, double max) {}

    private static final List<PlantillaPago> PLANTILLAS_PAGO = List.of(
            new PlantillaPago("Arriendo del local", "Pago mensual del arriendo", "GASTO", TipoOrigen.PROVEEDOR, 800, 4000),
            new PlantillaPago("Nómina", "Pago de salarios del personal", "EGRESO", TipoOrigen.TRABAJADOR, 1500, 9000),
            new PlantillaPago("Energía", "Factura de energía eléctrica", "GASTO", TipoOrigen.SERVICIO_PUBLICO, 80, 600),
            new PlantillaPago("Acueducto", "Factura de agua", "GASTO", TipoOrigen.SERVICIO_PUBLICO, 30, 250),
            new PlantillaPago("Internet", "Plan de internet y telefonía", "GASTO", TipoOrigen.SERVICIO_PUBLICO, 40, 200),
            new PlantillaPago("Gas", "Servicio de gas", "GASTO", TipoOrigen.SERVICIO_PUBLICO, 20, 150),
            new PlantillaPago("Seguro del negocio", "Póliza mensual", "EGRESO", TipoOrigen.FACTURA, 100, 700),
            new PlantillaPago("Publicidad", "Pauta en redes sociales", "GASTO", TipoOrigen.PROVEEDOR, 100, 1200),
            new PlantillaPago("Cuota del préstamo", "Abono mensual a crédito", "EGRESO", TipoOrigen.FACTURA, 300, 3000),
            new PlantillaPago("Suscripción de software", "Licencias y herramientas", "GASTO", TipoOrigen.PROVEEDOR, 20, 300),
            new PlantillaPago("Contrato mensual de servicio", "Ingreso recurrente de un cliente", "INGRESO", TipoOrigen.CLIENTE, 500, 5000)
    );

    @PostMapping("/api/test/generar-pagos")
    public ResponseEntity<String> generarPagos(
            @RequestParam(defaultValue = "3") int minimo,
            @RequestParam(defaultValue = "6") int maximo,
            @RequestParam(defaultValue = "false") boolean limpiar) {

        if (minimo < 1 || maximo < minimo || maximo > PLANTILLAS_PAGO.size()) {
            return ResponseEntity.badRequest()
                    .body("Rango inválido: usa 1 <= minimo <= maximo <= " + PLANTILLAS_PAGO.size());
        }

        List<Negocio> negocios = negocioRepository.findAll();
        if (negocios.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay negocios. Genera usuarios primero.");
        }

        if (limpiar) pagoPeriodicoRepository.deleteAll();

        Faker faker = new Faker();
        List<PagoPeriodico> pagos = new ArrayList<>();

        for (Negocio negocio : negocios) {
            List<PlantillaPago> plantillas = new ArrayList<>(PLANTILLAS_PAGO);
            Collections.shuffle(plantillas, random);
            int cantidad = minimo + random.nextInt(maximo - minimo + 1);

            for (PlantillaPago p : plantillas.subList(0, cantidad)) {
                boolean ingreso = "INGRESO".equals(p.tipo());

                String nombreOrigen = switch (p.tipoOrigen()) {
                    case CLIENTE, TRABAJADOR -> faker.name().fullName();
                    case PROVEEDOR -> faker.company().name();
                    case SERVICIO_PUBLICO -> p.nombre();
                    default -> "Factura " + (1000 + random.nextInt(9000));
                };

                PagoPeriodico pago = new PagoPeriodico();
                pago.setNombre(p.nombre());
                pago.setDescripcion(p.descripcion());
                pago.setMonto(redondear(p.min() + random.nextDouble() * (p.max() - p.min())));
                pago.setFechaPago(LocalDate.now().plusDays(random.nextInt(56) - 10)); // de -10 a +45 días
                pago.setActivo(random.nextDouble() < 0.85); // ~85% activos
                pago.setNegocioId(negocio.getIdNegocio());
                pago.setTipoMovimiento(TipoMovimiento.nuevo(p.tipo(),
                        ingreso ? NaturalezaMovimiento.CREDITO : NaturalezaMovimiento.DEBITO));
                pago.setOrigen(Origen.nuevo(nombreOrigen, p.tipoOrigen()));
                pagos.add(pago);
            }
        }

        pagoPeriodicoRepository.saveAll(pagos);
        return ResponseEntity.ok(pagos.size() + " pagos periódicos generados para " + negocios.size() + " negocios");
    }
}