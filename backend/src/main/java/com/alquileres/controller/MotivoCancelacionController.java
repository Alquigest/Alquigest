package com.alquileres.controller;

import com.alquileres.dto.MotivoCancelacionDTO;
import com.alquileres.service.MotivoCancelacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motivos-cancelacion")
@Tag(name = "Motivos de Cancelación", description = "API para gestionar motivos de cancelación de contratos")
public class MotivoCancelacionController {

    private final MotivoCancelacionService motivoCancelacionService;

    public MotivoCancelacionController(MotivoCancelacionService motivoCancelacionService) {
        this.motivoCancelacionService = motivoCancelacionService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los motivos de cancelación",
               description = "Retorna una lista con todos los motivos de cancelación disponibles")
    public ResponseEntity<List<MotivoCancelacionDTO>> obtenerTodosLosMotivos() {
        List<MotivoCancelacionDTO> motivos = motivoCancelacionService.obtenerTodosLosMotivos();
        return ResponseEntity.ok(motivos);
    }
}

