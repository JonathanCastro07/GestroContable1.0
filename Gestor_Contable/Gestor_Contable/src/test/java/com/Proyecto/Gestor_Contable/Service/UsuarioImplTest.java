package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.dtos.LoginRequest;
import com.Proyecto.Gestor_Contable.dtos.LoginResponse;
import com.Proyecto.Gestor_Contable.dtos.RegistroRequest;
import com.Proyecto.Gestor_Contable.dtos.RegistroResponse;
import com.Proyecto.Gestor_Contable.exception.CredencialesInvalidasException;
import com.Proyecto.Gestor_Contable.exception.EmailYaRegistradoException;
import com.Proyecto.Gestor_Contable.mapper.NegocioMapper;
import com.Proyecto.Gestor_Contable.modelo.Negocio;
import com.Proyecto.Gestor_Contable.modelo.Usuario;
import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.security.CustomUserDetailsService;
import com.Proyecto.Gestor_Contable.security.JwtUtil;
import com.Proyecto.Gestor_Contable.service.lmpl.LogAuditoriaServiceImpl;
import com.Proyecto.Gestor_Contable.service.lmpl.UsuarioImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioImplTest {

    @Mock NegocioRepository negocioRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock CustomUserDetailsService customUserDetailsService;
    @Mock JwtUtil jwtUtil;
    @Mock LogAuditoriaServiceImpl logAuditoria;

    @Spy
    NegocioMapper negocioMapper = new NegocioMapper();

    @InjectMocks
    UsuarioImpl service;

    private RegistroRequest crearRegistro() {
        return new RegistroRequest("Jonathan", "jonathan@gmail.com", "1234", "3001234567",
                "Mascota?", "Firulais", "Tienda Jonathan", "COMERCIO", 1000000, "PROPIETARIO");
    }

    private Negocio crearNegocio() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Jonathan");
        usuario.setCorreo("jonathan@gmail.com");
        usuario.setPassword("hash1234");

        Negocio negocio = new Negocio();
        negocio.setIdNegocio("n1");
        negocio.setNombreNegocio("Tienda Jonathan");
        negocio.setUsuario(usuario);
        return negocio;
    }

    @Test
    void registrarse_guardaElUsuarioConLaContrasenaEncriptada() {
        when(negocioRepository.existsByUsuario_Correo("jonathan@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("hash1234");
        when(negocioRepository.save(any(Negocio.class))).thenAnswer(inv -> inv.getArgument(0));

        RegistroResponse response = service.registrarse(crearRegistro());

        assertThat(response.correo()).isEqualTo("jonathan@gmail.com");
        assertThat(response.nombreNegocio()).isEqualTo("Tienda Jonathan");
        verify(negocioRepository).save(argThat(n -> n.getUsuario().getPassword().equals("hash1234")));
    }

    @Test
    void registrarse_conCorreoRepetido_lanzaError() {
        when(negocioRepository.existsByUsuario_Correo("jonathan@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> service.registrarse(crearRegistro()))
                .isInstanceOf(EmailYaRegistradoException.class);

        verify(negocioRepository, never()).save(any());
    }

    @Test
    void iniciaSesion_conDatosCorrectos_devuelveToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(negocioRepository.findFirstByUsuario_Correo("jonathan@gmail.com")).thenReturn(Optional.of(crearNegocio()));
        when(passwordEncoder.matches("1234", "hash1234")).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername("jonathan@gmail.com")).thenReturn(userDetails);
        when(jwtUtil.generarToken(userDetails)).thenReturn("token123");

        LoginResponse response = service.iniciaSesion(new LoginRequest("jonathan@gmail.com", "1234"));

        assertThat(response.token()).isEqualTo("token123");
        assertThat(response.nombre()).isEqualTo("Jonathan");
    }

    @Test
    void iniciaSesion_conContrasenaIncorrecta_lanzaError() {
        when(negocioRepository.findFirstByUsuario_Correo("jonathan@gmail.com")).thenReturn(Optional.of(crearNegocio()));
        when(passwordEncoder.matches("mala", "hash1234")).thenReturn(false);

        assertThatThrownBy(() -> service.iniciaSesion(new LoginRequest("jonathan@gmail.com", "mala")))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void iniciaSesion_conCorreoQueNoExiste_lanzaError() {
        when(negocioRepository.findFirstByUsuario_Correo("nadie@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.iniciaSesion(new LoginRequest("nadie@gmail.com", "1234")))
                .isInstanceOf(CredencialesInvalidasException.class);
    }
}
