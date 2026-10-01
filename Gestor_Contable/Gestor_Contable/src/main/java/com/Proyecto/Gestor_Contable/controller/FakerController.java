package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.modelo.Usuario;
import com.Proyecto.Gestor_Contable.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.Proyecto.Gestor_Contable.dtos.LoginRequest;
import com.Proyecto.Gestor_Contable.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FakerController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioService usuarioService;

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
                // login fallido, no se cuenta
            }
        }

        return ResponseEntity.ok(
                "Intentados: " + totalIntentados + " | Exitosos: " + exitosos
        );
    }
}