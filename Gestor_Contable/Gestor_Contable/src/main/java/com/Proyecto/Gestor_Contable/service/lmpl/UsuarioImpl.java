package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.LoginRequest;
import com.Proyecto.Gestor_Contable.dtos.LoginResponse;
import com.Proyecto.Gestor_Contable.dtos.RegistroRequest;
import com.Proyecto.Gestor_Contable.dtos.UsuarioResponse;
import com.Proyecto.Gestor_Contable.exception.CredencialesInvalidasException;
import com.Proyecto.Gestor_Contable.exception.EmailYaRegistradoException;
import com.Proyecto.Gestor_Contable.exception.UsuarioNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.MapperUsuario;
import com.Proyecto.Gestor_Contable.modelo.Usuario;
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