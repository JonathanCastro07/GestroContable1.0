package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.DTO.LoginRequest;
import com.Proyecto.Gestor_Contable.DTO.LoginResponse;
import com.Proyecto.Gestor_Contable.DTO.RegistroRequest;
import com.Proyecto.Gestor_Contable.DTO.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse registrarse(RegistroRequest request);
    LoginResponse iniciaSesion(LoginRequest request);
    List<UsuarioResponse> listarTodo();
    UsuarioResponse buscarPorId(String id);
    UsuarioResponse actualizar(String id, RegistroRequest request);
    void eliminar(String id);
}