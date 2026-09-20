package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.DTO.LoginRequest;
import com.Proyecto.Gestor_Contable.DTO.LoginResponse;
import com.Proyecto.Gestor_Contable.DTO.RegistroRequest;
import com.Proyecto.Gestor_Contable.DTO.UsuarioResponse;
import com.Proyecto.Gestor_Contable.Exception.CredencialesInvalidasException;
import com.Proyecto.Gestor_Contable.Exception.EmailYaRegistradoException;
import com.Proyecto.Gestor_Contable.Exception.UsuarioNoEncontradoException;
import com.Proyecto.Gestor_Contable.Mapper.MapperUsuario;
import com.Proyecto.Gestor_Contable.Modelo.Usuario;
import com.Proyecto.Gestor_Contable.Repository.UsuarioRepository;
import com.Proyecto.Gestor_Contable.Security.CustomUserDetailsService;
import com.Proyecto.Gestor_Contable.Security.JwtUtil;
import com.Proyecto.Gestor_Contable.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperUsuario mapperUsuario;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public UsuarioResponse registrarse(RegistroRequest request) {
        if (usuarioRepository.existsByCorreo(request.Correo())) {
            throw new EmailYaRegistradoException("El correo ya está registrado");
        }

        Usuario usuario = mapperUsuario.toEntity(request);
        usuario.setPassword(passwordEncoder.encode(request.password()));

        Usuario guardado = usuarioRepository.save(usuario);
        return mapperUsuario.toResponse(guardado);
    }

    @Override
    public LoginResponse iniciaSesion(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.email())
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(usuario.getCorreo());
        String token = jwtUtil.generarToken(userDetails);

        return new LoginResponse(usuario.getNombre(), usuario.getCorreo(), token);
    }

    @Override
    public List<UsuarioResponse> listarTodo() {
        return usuarioRepository.findAll()
                .stream()
                .map(mapperUsuario::toResponse)
                .toList();
    }

    @Override
    public UsuarioResponse buscarPorId(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con id: " + id));
        return mapperUsuario.toResponse(usuario);
    }

    @Override
    public UsuarioResponse actualizar(String id, RegistroRequest request) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con id: " + id));

        existente.setNombre(request.nombre());
        existente.setCorreo(request.Correo());
        existente.setPreguntaSeguridad(request.preguntaSeguridad());
        if (request.password() != null && !request.password().isBlank()) {
            existente.setPassword(passwordEncoder.encode(request.password()));
        }

        Usuario actualizado = usuarioRepository.save(existente);
        return mapperUsuario.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}