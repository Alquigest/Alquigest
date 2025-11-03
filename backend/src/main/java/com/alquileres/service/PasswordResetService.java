package com.alquileres.service;

import com.alquileres.model.Usuario;
import com.alquileres.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.password-reset-token-expiration-ms:3600000}")
    private long tokenExpirationTime;

    /**
     * Solicita recuperación de contraseña.
     * Este método retorna inmediatamente sin revelar si el email existe o no.
     * El procesamiento del email se realiza de forma asíncrona en segundo plano.
     *
     * @param email Email del usuario
     */
    public void solicitarRecuperacionContrasena(String email) {
        // Delegar el procesamiento real a un método asíncrono
        procesarRecuperacionAsync(email);

        // Retornar inmediatamente sin revelar información
        logger.info("Solicitud de recuperación de contraseña recibida para: {}", email);
    }

    /**
     * Procesa la recuperación de contraseña de forma asíncrona.
     * Se ejecuta en un hilo separado, por lo que no bloquea la respuesta al cliente.
     *
     * @param email Email del usuario
     */
    @Async
    protected void procesarRecuperacionAsync(String email) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

            if (usuario.isEmpty()) {
                logger.warn("Intento de recuperación con email no registrado: {}", email);
                // No hacer nada, pero tampoco revelar que el email no existe
                return;
            }

            Usuario u = usuario.get();
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusNanos(tokenExpirationTime * 1_000_000L);

            u.setPasswordResetToken(token);
            u.setPasswordResetTokenExpiry(expiryDate);
            usuarioRepository.save(u);

            emailService.enviarEmailRecuperacionContrasena(email, u.getUsername(), token);
            logger.info("Email de recuperación de contraseña enviado a: {}", email);

        } catch (Exception e) {
            logger.error("Error al procesar recuperación de contraseña para {}: {}", email, e.getMessage(), e);
        }
    }

    public void resetearContrasena(String token, String nuevaContrasena, String confirmarContrasena) {
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        Optional<Usuario> usuario = usuarioRepository.findByPasswordResetToken(token);

        if (usuario.isEmpty()) {
            logger.warn("Intento de reset con token inválido");
            throw new RuntimeException("Token de recuperación inválido");
        }

        Usuario u = usuario.get();

        if (u.getPasswordResetTokenExpiry() == null || LocalDateTime.now().isAfter(u.getPasswordResetTokenExpiry())) {
            logger.warn("Token expirado para usuario: {}", u.getUsername());
            throw new RuntimeException("El token de recuperación ha expirado");
        }

        u.setPassword(passwordEncoder.encode(nuevaContrasena));
        u.setPasswordResetToken(null);
        u.setPasswordResetTokenExpiry(null);
        usuarioRepository.save(u);

        logger.info("Contraseña reseteada exitosamente para usuario: {}", u.getUsername());
    }

    public boolean validarToken(String token) {
        Optional<Usuario> usuario = usuarioRepository.findByPasswordResetToken(token);

        if (usuario.isEmpty()) {
            return false;
        }

        Usuario u = usuario.get();
        return u.getPasswordResetTokenExpiry() != null && LocalDateTime.now().isBefore(u.getPasswordResetTokenExpiry());
    }
}

