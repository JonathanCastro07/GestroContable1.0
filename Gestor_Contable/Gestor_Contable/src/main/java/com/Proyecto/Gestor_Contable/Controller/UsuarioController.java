package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.Modelo.Usuario;
import com.Proyecto.Gestor_Contable.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarse(@RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body( usuarioService.registrarse(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> iniciaSesion(@RequestBody Usuario usuario){
        return ResponseEntity.ok(usuarioService.iniciaSesion(
                usuario.getCorreo(),
                usuario.getPassword()));

    }
    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        return ResponseEntity.ok(usuarioService.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.buscarPorid(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario){
        return  ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        usuarioService.eliminar(id);
        return  ResponseEntity.noContent().build();
    }

}
