package com.Proyecto.Gestor_Contable.Service;

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
import com.Proyecto.Gestor_Contable.service.lmpl.UsuarioImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioImplTest {

    @Mock UsuarioRepository usuarioRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock CustomUserDetailsService customUserDetailsService;
    @Mock JwtUtil jwtUtil;

    @Spy
    MapperUsuario mapperUsuario = new MapperUsuario();

    @InjectMocks
    UsuarioImpl service;



    private Usuario crearJonathan() {
        Usuario u = new Usuario();
        u.setIdUsuario("u1");
        u.setNombre("Jonathan");
        u.setCorreo("jonathan@gmail.com");
        u.setPassword("hashJonathan");
        u.setPreguntaSeguridad("Mascota?");
        return u;
    }

    private Usuario crearJuan() {
        Usuario u = new Usuario();
        u.setIdUsuario("u2");
        u.setNombre("Juan");
        u.setCorreo("juan@gmail.com");
        u.setPassword("hashJuan");
        u.setPreguntaSeguridad("Color?");
        return u;
    }



    @Test
    void registrarse_guardaUsuarioConPasswordEncriptada() {
        var request = new RegistroRequest("Jonathan", "jonathan@gmail.com", "1234", "Mascota?");
        when(usuarioRepository.existsByCorreo("jonathan@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("hashJonathan");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setIdUsuario("u1");
            return u;
        });

        UsuarioResponse response = service.registrarse(request);

        assertThat(response.idUsuario()).isEqualTo("u1");
        assertThat(response.nombre()).isEqualTo("Jonathan");
        assertThat(response.correo()).isEqualTo("jonathan@gmail.com");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("hashJonathan");
    }

    @Test
    void registrarse_dosUsuariosDistintos_seGuardanAmbos() {
        var jonathan = new RegistroRequest("Jonathan", "jonathan@gmail.com", "1234", "Mascota?");
        var juan = new RegistroRequest("Juan", "juan@gmail.com", "abcd", "Color?");

        when(usuarioRepository.existsByCorreo(any())).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("hashJonathan");
        when(passwordEncoder.encode("abcd")).thenReturn("hashJuan");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponse r1 = service.registrarse(jonathan);
        UsuarioResponse r2 = service.registrarse(juan);

        assertThat(r1.correo()).isEqualTo("jonathan@gmail.com");
        assertThat(r2.correo()).isEqualTo("juan@gmail.com");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository, times(2)).save(captor.capture());
        assertThat(captor.getAllValues())
                .extracting(Usuario::getNombre, Usuario::getPassword)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple("Jonathan", "hashJonathan"),
                        org.assertj.core.groups.Tuple.tuple("Juan", "hashJuan"));
    }

    @Test
    void registrarse_correoRepetido_lanzaExcepcion() {
        var request = new RegistroRequest("Juan", "jonathan@gmail.com", "abcd", "Color?");
        when(usuarioRepository.existsByCorreo("jonathan@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> service.registrarse(request))
                .isInstanceOf(EmailYaRegistradoException.class);

        verify(usuarioRepository, never()).save(any());
    }



    @Test
    void iniciaSesion_jonathanCredencialesCorrectas_devuelveToken() {
        UserDetails userDetails = User.withUsername("jonathan@gmail.com")
                .password("hashJonathan").roles("USER").build();

        when(usuarioRepository.findByCorreo("jonathan@gmail.com")).thenReturn(Optional.of(crearJonathan()));
        when(passwordEncoder.matches("1234", "hashJonathan")).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername("jonathan@gmail.com")).thenReturn(userDetails);
        when(jwtUtil.generarToken(userDetails)).thenReturn("tokenJonathan");

        LoginResponse response = service.iniciaSesion(new LoginRequest("jonathan@gmail.com", "1234"));

        assertThat(response.nombre()).isEqualTo("Jonathan");
        assertThat(response.correo()).isEqualTo("jonathan@gmail.com");
        assertThat(response.token()).isEqualTo("tokenJonathan");
    }

    @Test
    void iniciaSesion_juanCredencialesCorrectas_devuelveSuPropioToken() {
        UserDetails userDetails = User.withUsername("juan@gmail.com")
                .password("hashJuan").roles("USER").build();

        when(usuarioRepository.findByCorreo("juan@gmail.com")).thenReturn(Optional.of(crearJuan()));
        when(passwordEncoder.matches("abcd", "hashJuan")).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername("juan@gmail.com")).thenReturn(userDetails);
        when(jwtUtil.generarToken(userDetails)).thenReturn("tokenJuan");

        LoginResponse response = service.iniciaSesion(new LoginRequest("juan@gmail.com", "abcd"));

        assertThat(response.nombre()).isEqualTo("Juan");
        assertThat(response.correo()).isEqualTo("juan@gmail.com");
        assertThat(response.token()).isEqualTo("tokenJuan");
    }

    @Test
    void iniciaSesion_correoNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findByCorreo("nadie@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.iniciaSesion(new LoginRequest("nadie@gmail.com", "1234")))
                .isInstanceOf(CredencialesInvalidasException.class);

        verify(jwtUtil, never()).generarToken(any());
    }

    @Test
    void iniciaSesion_passwordIncorrecta_lanzaExcepcion() {
        when(usuarioRepository.findByCorreo("jonathan@gmail.com")).thenReturn(Optional.of(crearJonathan()));
        when(passwordEncoder.matches("mala", "hashJonathan")).thenReturn(false);

        assertThatThrownBy(() -> service.iniciaSesion(new LoginRequest("jonathan@gmail.com", "mala")))
                .isInstanceOf(CredencialesInvalidasException.class);

        verify(jwtUtil, never()).generarToken(any());
    }



    @Test
    void listarTodo_devuelveJonathanYJuan() {
        when(usuarioRepository.findAll()).thenReturn(List.of(crearJonathan(), crearJuan()));

        List<UsuarioResponse> lista = service.listarTodo();

        assertThat(lista).hasSize(2);
        assertThat(lista).extracting(UsuarioResponse::nombre).containsExactly("Jonathan", "Juan");
        assertThat(lista).extracting(UsuarioResponse::correo)
                .containsExactly("jonathan@gmail.com", "juan@gmail.com");
    }



    @Test
    void buscarPorId_existente_devuelveJuan() {
        when(usuarioRepository.findById("u2")).thenReturn(Optional.of(crearJuan()));

        UsuarioResponse response = service.buscarPorId("u2");

        assertThat(response.idUsuario()).isEqualTo("u2");
        assertThat(response.nombre()).isEqualTo("Juan");
        assertThat(response.correo()).isEqualTo("juan@gmail.com");
    }

    @Test
    void buscarPorId_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId("x"))
                .isInstanceOf(UsuarioNoEncontradoException.class);
    }



    @Test
    void actualizar_conPassword_laEncripta() {
        var request = new RegistroRequest("Jonathan Castro", "jonathan.castro@gmail.com", "nueva", "Color?");
        when(usuarioRepository.findById("u1")).thenReturn(Optional.of(crearJonathan()));
        when(passwordEncoder.encode("nueva")).thenReturn("hashNuevo");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponse response = service.actualizar("u1", request);

        assertThat(response.nombre()).isEqualTo("Jonathan Castro");
        assertThat(response.correo()).isEqualTo("jonathan.castro@gmail.com");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("hashNuevo");
        assertThat(captor.getValue().getPreguntaSeguridad()).isEqualTo("Color?");
    }

    @Test
    void actualizar_passwordVacia_noCambiaLaPassword() {
        var request = new RegistroRequest("Juan Perez", "juan@gmail.com", "  ", "Color?");
        when(usuarioRepository.findById("u2")).thenReturn(Optional.of(crearJuan()));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        service.actualizar("u2", request);

        verify(passwordEncoder, never()).encode(any());
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("hashJuan");
        assertThat(captor.getValue().getNombre()).isEqualTo("Juan Perez");
    }

    @Test
    void actualizar_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar("x",
                new RegistroRequest("A", "a@gmail.com", "1", "p")))
                .isInstanceOf(UsuarioNoEncontradoException.class);

        verify(usuarioRepository, never()).save(any());
    }



    @Test
    void eliminar_existente_borraElUsuario() {
        when(usuarioRepository.existsById("u2")).thenReturn(true);

        service.eliminar("u2");

        verify(usuarioRepository).deleteById("u2");
    }

    @Test
    void eliminar_noExiste_lanzaExcepcion() {
        when(usuarioRepository.existsById("x")).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar("x"))
                .isInstanceOf(UsuarioNoEncontradoException.class);

        verify(usuarioRepository, never()).deleteById(any());
    }
}