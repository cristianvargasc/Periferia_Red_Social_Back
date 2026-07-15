package com.redsocial.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Respuesta del endpoint de inicio de sesión.
 * Contiene el token JWT y los datos básicos del usuario autenticado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /** Token JWT para autenticar las siguientes solicitudes */
    private String token;

    /** Identificador único del usuario */
    private Long id;

    /** Nombre de usuario para autenticación */
    private String username;

    /** Alias público del usuario en la red social */
    private String alias;

    /** Nombres del usuario */
    private String nombres;

    /** Apellidos del usuario */
    private String apellidos;

    /** Fecha de nacimiento del usuario */
    private LocalDate fechaNacimiento;
}
