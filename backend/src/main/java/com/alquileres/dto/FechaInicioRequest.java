package com.alquileres.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO genérico para solicitudes con fecha de inicio
 * Se utiliza en endpoints que requieren especificar una fecha de inicio
 */
@Schema(description = "Solicitud con fecha de inicio")
public class FechaInicioRequest {

    @NotBlank(message = "La fecha de inicio es obligatoria")
    @Schema(description = "Fecha de inicio en formato ISO (yyyy-MM-dd)", example = "2025-01-15")
    private String fechaInicio;

    // Constructor vacío
    public FechaInicioRequest() {
    }

    // Constructor con parámetro
    public FechaInicioRequest(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    // Getters y Setters
    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}

