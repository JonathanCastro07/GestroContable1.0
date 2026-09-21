package com.Proyecto.Gestor_Contable.security;

import com.Proyecto.Gestor_Contable.modelo.Usuario;
import com.Proyecto.Gestor_Contable.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email)   {
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getCorreo())
                .password(usuario.getPassword())
                .roles("USER")
                .build();
    }
}
