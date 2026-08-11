package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.Modelo.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService  {
    Usuario registrarse(Usuario usuario);
    Usuario iniciaSesion(String correo, String pass);
    List<Usuario> listarTodo();
    Optional<Usuario> buscarPorid(Long id);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
}
