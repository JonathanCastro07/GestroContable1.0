package com.Proyecto.Gestor_Contable.security;

import com.Proyecto.Gestor_Contable.modelo.Negocio;
import com.Proyecto.Gestor_Contable.modelo.Usuario;
import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private NegocioRepository negocioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Negocio negocio = negocioRepository.findFirstByUsuario_Correo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User
                .withUsername(negocio.getUsuario().getCorreo())
                .password(negocio.getUsuario().getPassword())
                .roles("USER")
                .build();
    }
}
