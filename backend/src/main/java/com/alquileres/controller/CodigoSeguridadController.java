package com.alquileres.controller;

import com.alquileres.dto.*;
import com.alquileres.security.UserDetailsImpl;
import com.alquileres.service.CodigoSeguridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/codigos-seguridad")
@Tag(name = "Códigos de Seguridad", description = "API para gestión de códigos de seguridad de recuperación de contraseña")
public class CodigoSeguridadController {

    private static final Logger logger = LoggerFactory.getLogger(CodigoSeguridadController.class);

    private final CodigoSeguridadService codigoSeguridadService;

    public CodigoSeguridadController(CodigoSeguridadService codigoSeguridadService) {
        this.codigoSeguridadService = codigoSeguridadService;
    }

    @PostMapping("/regenerar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Regenerar códigos de seguridad",
        description = "Invalida los códigos actuales y genera nuevos códigos de seguridad para el usuario autenticado."
    )
    public ResponseEntity<?> regenerarCodigos(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long usuarioId = userDetails.getId();
            logger.info("Solicitud de regeneración de códigos para usuario ID: {}", usuarioId);

            List<String> codigos = codigoSeguridadService.regenerarCodigos(usuarioId);

            logger.info("Códigos regenerados exitosamente para usuario ID: {}", usuarioId);

            return ResponseEntity.ok(new CodigosSeguridadResponseDTO(
                codigos,
                "Códigos de seguridad regenerados exitosamente. IMPORTANTE: Guarde estos códigos en un lugar seguro. " +
                "No podrá volver a verlos una vez que cierre esta ventana."
            ));

        } catch (Exception e) {
            logger.error("Error al regenerar códigos de seguridad para usuario {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error al regenerar códigos: " + e.getMessage()));
        }
    }

    @PostMapping("/validar")
    @Operation(
        summary = "Validar código de seguridad",
        description = "Valida un código de seguridad y genera un token de recuperación de contraseña si es válido. " +
                      "Endpoint público para recuperación de contraseña."
    )
    public ResponseEntity<?> validarCodigo(@Valid @RequestBody ValidarCodigoSeguridadRequestDTO request) {
        try {
            String username = request.getUsername();
            String codigo = request.getCodigo();

            logger.info("Solicitud de validación de código para usuario: {}", username);

            Optional<String> tokenOpt = codigoSeguridadService.validarCodigoYGenerarToken(username, codigo);

            if (tokenOpt.isPresent()) {
                logger.info("Código validado exitosamente para usuario: {}", username);
                return ResponseEntity.ok(new ValidarCodigoSeguridadResponseDTO(
                    tokenOpt.get(),
                    "Código validado exitosamente. Use el token proporcionado para recuperar su contraseña.",
                    true
                ));
            } else {
                logger.warn("Código inválido o usuario sin códigos disponibles: {}", username);
                return ResponseEntity.badRequest()
                    .body(new ValidarCodigoSeguridadResponseDTO(
                        null,
                        "Código de seguridad inválido o no disponible. Verifique el código e intente nuevamente.",
                        false
                    ));
            }

        } catch (Exception e) {
            logger.error("Error al validar código de seguridad: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ValidarCodigoSeguridadResponseDTO(
                    null,
                    "Error al validar código: " + e.getMessage(),
                    false
                ));
        }
    }

    @GetMapping("/disponibles")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Consultar códigos disponibles",
        description = "Obtiene la cantidad de códigos de seguridad disponibles (no usados) para el usuario autenticado."
    )
    public ResponseEntity<?> consultarCodigosDisponibles(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Long usuarioId = userDetails.getId();
            logger.info("Consulta de códigos disponibles para usuario ID: {}", usuarioId);

            Long cantidad = codigoSeguridadService.contarCodigosDisponibles(usuarioId);

            String mensaje;
            if (cantidad == 0) {
                mensaje = "No tiene códigos de seguridad disponibles. Considere regenerar sus códigos.";
            } else if (cantidad == 1) {
                mensaje = "Tiene 1 código de seguridad disponible. Considere regenerar sus códigos pronto.";
            } else {
                mensaje = String.format("Tiene %d códigos de seguridad disponibles.", cantidad);
            }

            logger.info("Usuario ID: {} tiene {} códigos disponibles", usuarioId, cantidad);

            return ResponseEntity.ok(new CodigosDisponiblesResponseDTO(cantidad, mensaje));

        } catch (Exception e) {
            logger.error("Error al consultar códigos disponibles: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error al consultar códigos: " + e.getMessage()));
        }
    }

    @GetMapping("/disponibles/{usuarioId}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Consultar códigos disponibles de otro usuario",
        description = "Obtiene la cantidad de códigos de seguridad disponibles para un usuario específico. "
    )
    public ResponseEntity<?> consultarCodigosDisponiblesUsuario(
            @PathVariable Long usuarioId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            logger.info("Admin {} consultando códigos disponibles de usuario ID: {}",
                userDetails.getId(), usuarioId);

            Long cantidad = codigoSeguridadService.contarCodigosDisponibles(usuarioId);

            String mensaje;
            if (cantidad == 0) {
                mensaje = "El usuario no tiene códigos de seguridad disponibles.";
            } else if (cantidad == 1) {
                mensaje = "El usuario tiene 1 código de seguridad disponible.";
            } else {
                mensaje = String.format("El usuario tiene %d códigos de seguridad disponibles.", cantidad);
            }

            logger.info("Usuario ID: {} tiene {} códigos disponibles", usuarioId, cantidad);

            return ResponseEntity.ok(new CodigosDisponiblesResponseDTO(cantidad, mensaje));

        } catch (RuntimeException e) {
            logger.error("Error al consultar códigos disponibles para usuario {}: {}", usuarioId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al consultar códigos disponibles: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error al consultar códigos: " + e.getMessage()));
        }
    }
}
