package com.redsocial.back.controller;

import com.redsocial.back.dto.LoginResponse;
import com.redsocial.back.security.JwtUtils;
import com.redsocial.back.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.redsocial.back.model.Usuario;
import com.redsocial.back.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIniciarSesionExitoso() {
        String username = "testuser";
        String password = "password";
        String token = "jwtToken";

        Usuario user = new Usuario();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(password);
        user.setNombres("John");
        user.setApellidos("Doe");
        user.setFechaNacimiento(LocalDate.of(1995, 5, 15));
        user.setAlias("johndoe");

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generarToken(username)).thenReturn(token);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<LoginResponse> response = authController.iniciarSesion(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());
        assertEquals(username, response.getBody().getUsername());
        assertEquals("johndoe", response.getBody().getAlias());
    }
}
