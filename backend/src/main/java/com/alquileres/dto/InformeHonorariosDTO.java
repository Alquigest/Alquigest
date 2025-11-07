package com.alquileres.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para el Informe 1: Honorarios por inmueble y total del mes
 */
public class InformeHonorariosDTO {

    private String periodo;
    private List<HonorarioPorInmuebleDTO> honorariosPorInmueble;
    private BigDecimal totalHonorarios;

    public InformeHonorariosDTO() {
    }

    public InformeHonorariosDTO(String periodo, List<HonorarioPorInmuebleDTO> honorariosPorInmueble, BigDecimal totalHonorarios) {
        this.periodo = periodo;
        this.honorariosPorInmueble = honorariosPorInmueble;
        this.totalHonorarios = totalHonorarios;
    }

    // Getters y Setters
    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<HonorarioPorInmuebleDTO> getHonorariosPorInmueble() {
        return honorariosPorInmueble;
    }

    public void setHonorariosPorInmueble(List<HonorarioPorInmuebleDTO> honorariosPorInmueble) {
        this.honorariosPorInmueble = honorariosPorInmueble;
    }

    public BigDecimal getTotalHonorarios() {
        return totalHonorarios;
    }

    public void setTotalHonorarios(BigDecimal totalHonorarios) {
        this.totalHonorarios = totalHonorarios;
    }

    public static class HonorarioPorInmuebleDTO {
        private Long inmuebleId;
        private String direccionInmueble;
        private Long contratoId;
        private String nombrePropietario;
        private String apellidoPropietario;
        private String nombreInquilino;
        private String apellidoInquilino;
        private BigDecimal montoAlquiler;
        private BigDecimal honorario;

        public HonorarioPorInmuebleDTO() {
        }

        // Getters y Setters
        public Long getInmuebleId() {
            return inmuebleId;
        }

        public void setInmuebleId(Long inmuebleId) {
            this.inmuebleId = inmuebleId;
        }

        public String getDireccionInmueble() {
            return direccionInmueble;
        }

        public void setDireccionInmueble(String direccionInmueble) {
            this.direccionInmueble = direccionInmueble;
        }

        public Long getContratoId() {
            return contratoId;
        }

        public void setContratoId(Long contratoId) {
            this.contratoId = contratoId;
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

        public BigDecimal getMontoAlquiler() {
            return montoAlquiler;
        }

        public void setMontoAlquiler(BigDecimal montoAlquiler) {
            this.montoAlquiler = montoAlquiler;
        }

        public BigDecimal getHonorario() {
            return honorario;
        }

        public void setHonorario(BigDecimal honorario) {
            this.honorario = honorario;
        }
    }
}

