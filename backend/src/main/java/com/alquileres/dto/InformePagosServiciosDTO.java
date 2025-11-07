package com.alquileres.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para el Informe 4: Pagos de servicios con detalle completo
 * Los pagos están agrupados por contrato
 */
public class InformePagosServiciosDTO {

    private String periodo;
    private List<PagosServiciosPorContratoDTO> contratosPagosServicios;
    private BigDecimal totalPagado;

    public InformePagosServiciosDTO() {
    }

    public InformePagosServiciosDTO(String periodo, List<PagosServiciosPorContratoDTO> contratosPagosServicios, BigDecimal totalPagado) {
        this.periodo = periodo;
        this.contratosPagosServicios = contratosPagosServicios;
        this.totalPagado = totalPagado;
    }

    // Getters y Setters
    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<PagosServiciosPorContratoDTO> getContratosPagosServicios() {
        return contratosPagosServicios;
    }

    public void setContratosPagosServicios(List<PagosServiciosPorContratoDTO> contratosPagosServicios) {
        this.contratosPagosServicios = contratosPagosServicios;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    /**
     * DTO que agrupa los pagos de servicios por contrato
     */
    public static class PagosServiciosPorContratoDTO {
        private Long contratoId;
        private String direccionInmueble;
        private String nombrePropietario;
        private String apellidoPropietario;
        private String nombreInquilino;
        private String apellidoInquilino;
        private AlquilerRelacionadoDTO alquilerRelacionado;
        private List<PagoServicioDetalleDTO> pagosServicios;
        private BigDecimal subtotalPagado;

        public PagosServiciosPorContratoDTO() {
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

        public AlquilerRelacionadoDTO getAlquilerRelacionado() {
            return alquilerRelacionado;
        }

        public void setAlquilerRelacionado(AlquilerRelacionadoDTO alquilerRelacionado) {
            this.alquilerRelacionado = alquilerRelacionado;
        }

        public List<PagoServicioDetalleDTO> getPagosServicios() {
            return pagosServicios;
        }

        public void setPagosServicios(List<PagoServicioDetalleDTO> pagosServicios) {
            this.pagosServicios = pagosServicios;
        }

        public BigDecimal getSubtotalPagado() {
            return subtotalPagado;
        }

        public void setSubtotalPagado(BigDecimal subtotalPagado) {
            this.subtotalPagado = subtotalPagado;
        }
    }

    public static class PagoServicioDetalleDTO {
        private Integer pagoServicioId;
        private String fechaPago;
        private BigDecimal monto;
        private String periodoServicio;
        private String tipoServicio;
        private Boolean estaPagado;

        public PagoServicioDetalleDTO() {
        }

        // Getters y Setters
        public Integer getPagoServicioId() {
            return pagoServicioId;
        }

        public void setPagoServicioId(Integer pagoServicioId) {
            this.pagoServicioId = pagoServicioId;
        }

        public String getFechaPago() {
            return fechaPago;
        }

        public void setFechaPago(String fechaPago) {
            this.fechaPago = fechaPago;
        }

        public BigDecimal getMonto() {
            return monto;
        }

        public void setMonto(BigDecimal monto) {
            this.monto = monto;
        }

        public String getPeriodoServicio() {
            return periodoServicio;
        }

        public void setPeriodoServicio(String periodoServicio) {
            this.periodoServicio = periodoServicio;
        }

        public String getTipoServicio() {
            return tipoServicio;
        }

        public void setTipoServicio(String tipoServicio) {
            this.tipoServicio = tipoServicio;
        }

        public Boolean getEstaPagado() {
            return estaPagado;
        }

        public void setEstaPagado(Boolean estaPagado) {
            this.estaPagado = estaPagado;
        }
    }

    public static class AlquilerRelacionadoDTO {
        private Long alquilerId;
        private BigDecimal montoAlquiler;
        private String fechaVencimientoAlquiler;
        private Boolean alquilerPagado;

        public AlquilerRelacionadoDTO() {
        }

        // Getters y Setters
        public Long getAlquilerId() {
            return alquilerId;
        }

        public void setAlquilerId(Long alquilerId) {
            this.alquilerId = alquilerId;
        }

        public BigDecimal getMontoAlquiler() {
            return montoAlquiler;
        }

        public void setMontoAlquiler(BigDecimal montoAlquiler) {
            this.montoAlquiler = montoAlquiler;
        }

        public String getFechaVencimientoAlquiler() {
            return fechaVencimientoAlquiler;
        }

        public void setFechaVencimientoAlquiler(String fechaVencimientoAlquiler) {
            this.fechaVencimientoAlquiler = fechaVencimientoAlquiler;
        }

        public Boolean getAlquilerPagado() {
            return alquilerPagado;
        }

        public void setAlquilerPagado(Boolean alquilerPagado) {
            this.alquilerPagado = alquilerPagado;
        }
    }
}

