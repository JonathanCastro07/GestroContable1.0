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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
}