package com.Proyecto.Gestor_Contable.service;

import com.Proyecto.Gestor_Contable.dtos.LoginRequest;
import com.Proyecto.Gestor_Contable.dtos.LoginResponse;
import com.Proyecto.Gestor_Contable.dtos.RegistroRequest;
import com.Proyecto.Gestor_Contable.dtos.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse registrarse(RegistroRequest request);
    LoginResponse iniciaSesion(LoginRequest request);
    List<UsuarioResponse> listarTodo();
    UsuarioResponse buscarPorId(String id);
    UsuarioResponse actualizar(String id, RegistroRequest request);
    void eliminar(String id);
}