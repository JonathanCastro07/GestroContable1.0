package com.Proyecto.Gestor_Contable.service;

import com.Proyecto.Gestor_Contable.dtos.*;


public interface UsuarioService {
    RegistroResponse registrarse(RegistroRequest request);
    LoginResponse iniciaSesion(LoginRequest request);
}