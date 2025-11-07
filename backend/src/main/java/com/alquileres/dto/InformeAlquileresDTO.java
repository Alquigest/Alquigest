package com.alquileres.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para el Informe 2: Pagos de alquileres del mes
 */
public class InformeAlquileresDTO {

    private String periodo;
    private List<PagoAlquilerDetalleDTO> pagos;
    private BigDecimal totalPagado;
    private BigDecimal montoPorCobrar;

    public InformeAlquileresDTO() {
    }

    public InformeAlquileresDTO(String periodo, List<PagoAlquilerDetalleDTO> pagos, BigDecimal totalPagado, BigDecimal montoPorCobrar) {
        this.periodo = periodo;
        this.pagos = pagos;
        this.totalPagado = totalPagado;
        this.montoPorCobrar = montoPorCobrar;
    }

    // Getters y Setters
    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<PagoAlquilerDetalleDTO> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoAlquilerDetalleDTO> pagos) {
        this.pagos = pagos;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getMontoPorCobrar() {
        return montoPorCobrar;
    }

    public void setMontoPorCobrar(BigDecimal montoPorCobrar) {
        this.montoPorCobrar = montoPorCobrar;
    }

    public static class PagoAlquilerDetalleDTO {
        private Long alquilerId;
        private Long contratoId;
        private String direccionInmueble;
        private String nombreInquilino;
        private String apellidoInquilino;
        private String nombrePropietario;
        private String apellidoPropietario;
        private BigDecimal monto;
        private String fechaPago;
        private String fechaVencimiento;
        private Boolean estaPagado;

        public PagoAlquilerDetalleDTO() {
        }

        // Getters y Setters
        public Long getAlquilerId() {
            return alquilerId;
        }

        public void setAlquilerId(Long alquilerId) {
            this.alquilerId = alquilerId;
        }

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

        public BigDecimal getMonto() {
            return monto;
        }

        public void setMonto(BigDecimal monto) {
            this.monto = monto;
        }

        public String getFechaPago() {
            return fechaPago;
        }

        public void setFechaPago(String fechaPago) {
            this.fechaPago = fechaPago;
        }

        public String getFechaVencimiento() {
            return fechaVencimiento;
        }

        public void setFechaVencimiento(String fechaVencimiento) {
            this.fechaVencimiento = fechaVencimiento;
        }

        public Boolean getEstaPagado() {
            return estaPagado;
        }

        public void setEstaPagado(Boolean estaPagado) {
            this.estaPagado = estaPagado;
        }
    }
}

