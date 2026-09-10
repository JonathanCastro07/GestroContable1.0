package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.Exception.CredencialesInvalidasException;
import com.Proyecto.Gestor_Contable.Modelo.Usuario;
import com.Proyecto.Gestor_Contable.Repository.UsuarioRepository;
import com.Proyecto.Gestor_Contable.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrarse(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())){
            throw new RuntimeException("El correo ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario iniciaSesion(String correo, String pass) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
               .orElseThrow(()-> new RuntimeException("El correo no esta registrado"));

       if (!passwordEncoder.matches(pass, usuario.getPassword())){
           throw new CredencialesInvalidasException("COntraseña incorrecta");
       }
        return usuario;
    }

    @Override
    public List<Usuario> listarTodo() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorid(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id).
                orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setCorreo(usuario.getCorreo());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setPassword(usuario.getPassword());

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}

