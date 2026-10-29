package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.*;
import com.Proyecto.Gestor_Contable.exception.CredencialesInvalidasException;
import com.Proyecto.Gestor_Contable.exception.EmailYaRegistradoException;
import com.Proyecto.Gestor_Contable.exception.UsuarioNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.MapperUsuario;
import com.Proyecto.Gestor_Contable.mapper.NegocioMapper;
import com.Proyecto.Gestor_Contable.modelo.Negocio;
import com.Proyecto.Gestor_Contable.modelo.Usuario;
import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.repository.UsuarioRepository;
import com.Proyecto.Gestor_Contable.security.CustomUserDetailsService;
import com.Proyecto.Gestor_Contable.security.JwtUtil;
import com.Proyecto.Gestor_Contable.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioImpl implements UsuarioService {

    private final NegocioRepository negocioRepository;
    private final PasswordEncoder passwordEncoder;
    private final NegocioMapper negocioMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public RegistroResponse registrarse(RegistroRequest request) {
        if (negocioRepository.existsByUsuario_Correo(request.correo())) {
            throw new EmailYaRegistradoException("El correo ya está registrado");
        }

        Negocio negocio = negocioMapper.toEntityConUsuario(request);
        negocio.getUsuario().setPassword(passwordEncoder.encode(request.password()));

        Negocio guardado = negocioRepository.save(negocio);
        return negocioMapper.toRegistroResponse(guardado);
    }

    @Override
    public LoginResponse iniciaSesion(LoginRequest request) {
        Negocio negocio = negocioRepository.findFirstByUsuario_Correo(request.correo())
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.password(), negocio.getUsuario().getPassword())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(negocio.getUsuario().getCorreo());
        String token = jwtUtil.generarToken(userDetails);

        return new LoginResponse(negocio.getUsuario().getNombre(), negocio.getUsuario().getCorreo(), token);
    }
}