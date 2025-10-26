package com.alquileres.dto;

import com.alquileres.model.AumentoAlquiler;

import java.math.BigDecimal;

public class AumentoAlquilerDTO {

    private Long id;

    private Long contratoId;

    private String fechaAumento;

    private BigDecimal montoAnterior;

    private BigDecimal montoNuevo;

    private BigDecimal porcentajeAumento;

    private String descripcion;

    private String createdAt;

    // Constructor por defecto
    public AumentoAlquilerDTO() {
    }

    // Constructor desde entidad
    public AumentoAlquilerDTO(AumentoAlquiler aumentoAlquiler) {
        this.id = aumentoAlquiler.getId();
        this.contratoId = aumentoAlquiler.getContrato() != null ? aumentoAlquiler.getContrato().getId() : null;
        this.fechaAumento = aumentoAlquiler.getFechaAumento();
        this.montoAnterior = aumentoAlquiler.getMontoAnterior();
        this.montoNuevo = aumentoAlquiler.getMontoNuevo();
        this.porcentajeAumento = aumentoAlquiler.getPorcentajeAumento();
        this.descripcion = aumentoAlquiler.getDescripcion();
        this.createdAt = aumentoAlquiler.getCreatedAt();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContratoId() {
        return contratoId;
    }

    public void setContratoId(Long contratoId) {
        this.contratoId = contratoId;
    }

    public String getFechaAumento() {
        return fechaAumento;
    }

    public void setFechaAumento(String fechaAumento) {
        this.fechaAumento = fechaAumento;
    }

    public BigDecimal getMontoAnterior() {
        return montoAnterior;
    }

    public void setMontoAnterior(BigDecimal montoAnterior) {
        this.montoAnterior = montoAnterior;
    }

    public BigDecimal getMontoNuevo() {
        return montoNuevo;
    }

    public void setMontoNuevo(BigDecimal montoNuevo) {
        this.montoNuevo = montoNuevo;
    }

    public BigDecimal getPorcentajeAumento() {
        return porcentajeAumento;
    }

    public void setPorcentajeAumento(BigDecimal porcentajeAumento) {
        this.porcentajeAumento = porcentajeAumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

