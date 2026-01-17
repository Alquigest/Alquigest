package com.alquileres.service;

import com.alquileres.model.CodigoSeguridad;
import com.alquileres.model.Usuario;
import com.alquileres.repository.CodigoSeguridadRepository;
import com.alquileres.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CodigoSeguridadService {

    private static final Logger logger = LoggerFactory.getLogger(CodigoSeguridadService.class);
    private static final int CANTIDAD_CODIGOS = 8;
    private static final int LONGITUD_SEGMENTO = 4;
    private static final int CANTIDAD_SEGMENTOS = 3;
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    private final CodigoSeguridadRepository codigoSeguridadRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final long tokenExpirationTime;

    public CodigoSeguridadService(
            CodigoSeguridadRepository codigoSeguridadRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.codigoSeguridadRepository = codigoSeguridadRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenExpirationTime = 3600000L;
    }

    @Transactional
    public List<String> generarCodigos(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }

        Usuario usuario = usuarioOpt.get();

        if (codigoSeguridadRepository.existsByUsuarioAndUsadoFalse(usuario)) {
            logger.warn("El usuario {} ya tiene códigos de seguridad activos", usuario.getUsername());
            return List.of();
        }

        return generarYGuardarCodigos(usuario);
    }

    @Transactional
    public List<String> regenerarCodigos(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }

        Usuario usuario = usuarioOpt.get();

        codigoSeguridadRepository.invalidarCodigosPorUsuario(usuario, LocalDateTime.now());
        logger.info("Códigos anteriores invalidados para usuario: {}", usuario.getUsername());

        return generarYGuardarCodigos(usuario);
    }

    private List<String> generarYGuardarCodigos(Usuario usuario) {
        List<String> codigosPlanos = new ArrayList<>();

        for (int i = 0; i < CANTIDAD_CODIGOS; i++) {
            String codigoPlano = generarCodigoAleatorio();
            String codigoHash = passwordEncoder.encode(codigoPlano);

            CodigoSeguridad codigoSeguridad = new CodigoSeguridad(usuario, codigoHash);
            codigoSeguridadRepository.save(codigoSeguridad);

            codigosPlanos.add(codigoPlano);
        }

        logger.info("Generados {} códigos de seguridad para usuario: {}", CANTIDAD_CODIGOS, usuario.getUsername());
        return codigosPlanos;
    }

    private String generarCodigoAleatorio() {
        StringBuilder codigo = new StringBuilder();

        for (int segmento = 0; segmento < CANTIDAD_SEGMENTOS; segmento++) {
            if (segmento > 0) {
                codigo.append("-");
            }
            for (int i = 0; i < LONGITUD_SEGMENTO; i++) {
                int index = random.nextInt(CARACTERES.length());
                codigo.append(CARACTERES.charAt(index));
            }
        }

        return codigo.toString();
    }

    @Transactional
    public Optional<String> validarCodigoYGenerarToken(String username, String codigoPlano) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            logger.warn("Intento de validación de código para usuario inexistente: {}", username);
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();
        List<CodigoSeguridad> codigosDisponibles = codigoSeguridadRepository.findByUsuarioAndUsadoFalse(usuario);

        if (codigosDisponibles.isEmpty()) {
            logger.warn("Usuario {} no tiene códigos de seguridad disponibles", username);
            return Optional.empty();
        }

        for (CodigoSeguridad codigo : codigosDisponibles) {
            if (passwordEncoder.matches(codigoPlano, codigo.getCodigoHash())) {
                codigo.marcarComoUsado();
                codigoSeguridadRepository.save(codigo);

                String token = generarTokenRecuperacion();
                LocalDateTime expiryDate = LocalDateTime.now().plusNanos(tokenExpirationTime * 1_000_000L);
                usuario.setPasswordResetToken(token);
                usuario.setPasswordResetTokenExpiry(expiryDate);
                usuarioRepository.save(usuario);

                logger.info("Código de seguridad validado exitosamente para usuario: {}", username);
                return Optional.of(token);
            }
        }

        logger.warn("Código de seguridad inválido para usuario: {}", username);
        return Optional.empty();
    }

    private String generarTokenRecuperacion() {
        return UUID.randomUUID().toString();
    }

    public Long contarCodigosDisponibles(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }

        return codigoSeguridadRepository.countCodigosDisponibles(usuarioOpt.get());
    }

    public boolean tieneCodigosDisponibles(Long usuarioId) {
        return contarCodigosDisponibles(usuarioId) > 0;
    }
}
