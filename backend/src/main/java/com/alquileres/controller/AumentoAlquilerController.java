package com.alquileres.controller;

import com.alquileres.dto.AumentoAlquilerDTO;
import com.alquileres.service.AumentoAlquilerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aumentos")
@Tag(name = "Aumento Alquiler", description = "Endpoints para gestionar el historial de aumentos de alquileres")
public class AumentoAlquilerController {

    @Autowired
    private AumentoAlquilerService aumentoAlquilerService;

    /**
     * GET /api/aumentos/contrato/{contratoId} - Obtener historial completo de aumentos de un contrato
     */
    @GetMapping("/contrato/{contratoId}")
    @Operation(summary = "Obtener historial de aumentos de un contrato", description = "Retorna todos los aumentos registrados para un contrato específico, ordenados por fecha descendente")
    public ResponseEntity<List<AumentoAlquilerDTO>> obtenerHistorialAumentos(@PathVariable Long contratoId) {
        List<AumentoAlquilerDTO> aumentos = aumentoAlquilerService.obtenerHistorialAumentos(contratoId);
        return ResponseEntity.ok(aumentos);
    }

    /**
     * GET /api/aumentos/{id} - Obtener un aumento específico
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un aumento específico", description = "Retorna los detalles de un aumento de alquiler por su ID")
    public ResponseEntity<AumentoAlquilerDTO> obtenerAumento(@PathVariable Long id) {
        AumentoAlquilerDTO aumento = aumentoAlquilerService.obtenerAumentoPorId(id);
        return ResponseEntity.ok(aumento);
    }

    /**
     * GET /api/aumentos/contrato/{contratoId}/ultimo - Obtener último aumento de un contrato
     */
    @GetMapping("/contrato/{contratoId}/ultimo")
    @Operation(summary = "Obtener el último aumento de un contrato", description = "Retorna el aumento más reciente registrado para un contrato")
    public ResponseEntity<AumentoAlquilerDTO> obtenerUltimoAumento(@PathVariable Long contratoId) {
        AumentoAlquilerDTO aumento = aumentoAlquilerService.obtenerUltimoAumento(contratoId);
        return ResponseEntity.ok(aumento);
    }

    /**
     * GET /api/aumentos/contrato/{contratoId}/rango - Obtener aumentos en un rango de fechas
     */
    @GetMapping("/contrato/{contratoId}/rango")
    @Operation(summary = "Obtener aumentos en un rango de fechas", description = "Retorna los aumentos de un contrato dentro de un rango de fechas específico")
    public ResponseEntity<List<AumentoAlquilerDTO>> obtenerAumentosPorRangoFechas(
            @PathVariable Long contratoId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        List<AumentoAlquilerDTO> aumentos = aumentoAlquilerService.obtenerAumentosPorRangoFechas(contratoId, fechaInicio, fechaFin);
        return ResponseEntity.ok(aumentos);
    }

    /**
     * GET /api/aumentos/contrato/{contratoId}/contar - Contar aumentos de un contrato
     */
    @GetMapping("/contrato/{contratoId}/contar")
    @Operation(summary = "Contar aumentos de un contrato", description = "Retorna la cantidad total de aumentos registrados para un contrato")
    public ResponseEntity<Long> contarAumentos(@PathVariable Long contratoId) {
        Long cantidad = aumentoAlquilerService.contarAumentos(contratoId);
        return ResponseEntity.ok(cantidad);
    }

    /**
     * POST /api/aumentos/contrato/{contratoId} - Registrar manualmente un nuevo aumento
     */
    @PostMapping("/contrato/{contratoId}")
    @Operation(summary = "Registrar un nuevo aumento manualmente", description = "Registra un nuevo aumento de alquiler para un contrato específico")
    public ResponseEntity<AumentoAlquilerDTO> registrarAumento(
            @PathVariable Long contratoId,
            @RequestBody AumentoAlquilerDTO aumentoDTO) {
        AumentoAlquilerDTO aumentoRegistrado = aumentoAlquilerService.registrarAumento(contratoId, aumentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(aumentoRegistrado);
    }
}
