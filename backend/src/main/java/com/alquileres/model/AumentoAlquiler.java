package com.alquileres.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "aumento_alquiler")
public class AumentoAlquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Column(name = "fecha_aumento", nullable = false)
    private String fechaAumento;

    @Column(name = "monto_anterior", nullable = false)
    private BigDecimal montoAnterior;

    @Column(name = "monto_nuevo", nullable = false)
    private BigDecimal montoNuevo;

    @Column(name = "porcentaje_aumento", nullable = false)
    private BigDecimal porcentajeAumento;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "created_at")
    private String createdAt;

    // Constructores
    public AumentoAlquiler() {
    }

    public AumentoAlquiler(Contrato contrato, String fechaAumento, BigDecimal montoAnterior,
                           BigDecimal montoNuevo, BigDecimal porcentajeAumento) {
        this.contrato = contrato;
        this.fechaAumento = fechaAumento;
        this.montoAnterior = montoAnterior;
        this.montoNuevo = montoNuevo;
        this.porcentajeAumento = porcentajeAumento;
        this.createdAt = java.time.LocalDateTime.now().toString();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
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

