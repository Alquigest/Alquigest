package com.alquileres.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para el Informe 3: Aumentos de alquiler por contrato de los últimos 6 meses
 */
public class InformeAumentosDTO {

    private String periodoDesde;
    private String periodoHasta;
    private List<AumentoPorContratoDTO> aumentosPorContrato;

    public InformeAumentosDTO() {
    }

    public InformeAumentosDTO(String periodoDesde, String periodoHasta, List<AumentoPorContratoDTO> aumentosPorContrato) {
        this.periodoDesde = periodoDesde;
        this.periodoHasta = periodoHasta;
        this.aumentosPorContrato = aumentosPorContrato;
    }

    // Getters y Setters
    public String getPeriodoDesde() {
        return periodoDesde;
    }

    public void setPeriodoDesde(String periodoDesde) {
        this.periodoDesde = periodoDesde;
    }

    public String getPeriodoHasta() {
        return periodoHasta;
    }

    public void setPeriodoHasta(String periodoHasta) {
        this.periodoHasta = periodoHasta;
    }

    public List<AumentoPorContratoDTO> getAumentosPorContrato() {
        return aumentosPorContrato;
    }

    public void setAumentosPorContrato(List<AumentoPorContratoDTO> aumentosPorContrato) {
        this.aumentosPorContrato = aumentosPorContrato;
    }

    public static class AumentoPorContratoDTO {
        private Long contratoId;
        private String direccionInmueble;
        private String nombreInquilino;
        private String apellidoInquilino;
        private String nombrePropietario;
        private String apellidoPropietario;
        private List<DetalleAumentoDTO> aumentos;

        public AumentoPorContratoDTO() {
        }

        // Getters y Setters
        public Long getContratoId() {
            return contratoId;
        }

        public void setContratoId(Long contratoId) {
            this.contratoId = contratoId;
        }

        public String getDireccionInmueble() {
            return direccionInmueble;
        }

        public void setDireccionInmueble(String direccionInmueble) {
            this.direccionInmueble = direccionInmueble;
        }

        public String getNombreInquilino() {
            return nombreInquilino;
        }

        public void setNombreInquilino(String nombreInquilino) {
            this.nombreInquilino = nombreInquilino;
        }

        public String getApellidoInquilino() {
            return apellidoInquilino;
        }

        public void setApellidoInquilino(String apellidoInquilino) {
            this.apellidoInquilino = apellidoInquilino;
        }

        public String getNombrePropietario() {
            return nombrePropietario;
        }

        public void setNombrePropietario(String nombrePropietario) {
            this.nombrePropietario = nombrePropietario;
        }

        public String getApellidoPropietario() {
            return apellidoPropietario;
        }

        public void setApellidoPropietario(String apellidoPropietario) {
            this.apellidoPropietario = apellidoPropietario;
        }

        public List<DetalleAumentoDTO> getAumentos() {
            return aumentos;
        }

        public void setAumentos(List<DetalleAumentoDTO> aumentos) {
            this.aumentos = aumentos;
        }
    }

    public static class DetalleAumentoDTO {
        private Long aumentoId;
        private String fechaAumento;
        private BigDecimal montoAnterior;
        private BigDecimal montoNuevo;
        private BigDecimal porcentajeAumento;

        public DetalleAumentoDTO() {
        }

        // Getters y Setters
        public Long getAumentoId() {
            return aumentoId;
        }

        public void setAumentoId(Long aumentoId) {
            this.aumentoId = aumentoId;
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
    }
}

