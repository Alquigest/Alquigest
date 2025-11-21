package com.alquileres.dto;

import java.util.List;

/**
 * DTO para la generación de recibos de servicios
 * Agrupa todos los datos necesarios para generar un recibo en un período específico
 */
public class ReciboServicioDTO {

    private String periodo;
    private List<ServicioReciboDTO> servicios;
    private ContratoReciboDTO contrato;
    private PersonaReciboDTO propietario;
    private PersonaReciboDTO inquilino;

    // Constructor vacío
    public ReciboServicioDTO() {
    }

    // Constructor con parámetros
    public ReciboServicioDTO(String periodo, List<ServicioReciboDTO> servicios,
                            ContratoReciboDTO contrato, PersonaReciboDTO propietario,
                            PersonaReciboDTO inquilino) {
        this.periodo = periodo;
        this.servicios = servicios;
        this.contrato = contrato;
        this.propietario = propietario;
        this.inquilino = inquilino;
    }

    // Getters y Setters
    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<ServicioReciboDTO> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioReciboDTO> servicios) {
        this.servicios = servicios;
    }

    public ContratoReciboDTO getContrato() {
        return contrato;
    }

    public void setContrato(ContratoReciboDTO contrato) {
        this.contrato = contrato;
    }

    public PersonaReciboDTO getPropietario() {
        return propietario;
    }

    public void setPropietario(PersonaReciboDTO propietario) {
        this.propietario = propietario;
    }

    public PersonaReciboDTO getInquilino() {
        return inquilino;
    }

    public void setInquilino(PersonaReciboDTO inquilino) {
        this.inquilino = inquilino;
    }

    @Override
    public String toString() {
        return "ReciboServicioDTO{" +
                "periodo='" + periodo + '\'' +
                ", servicios=" + servicios +
                ", contrato=" + contrato +
                ", propietario=" + propietario +
                ", inquilino=" + inquilino +
                '}';
    }

    /**
     * DTO interna para representar un servicio en el recibo
     */
    public static class ServicioReciboDTO {
        private Integer id;
        private String nombreTipoServicio;
        private java.math.BigDecimal monto;

        public ServicioReciboDTO() {
        }

        public ServicioReciboDTO(Integer id, String nombreTipoServicio, java.math.BigDecimal monto) {
            this.id = id;
            this.nombreTipoServicio = nombreTipoServicio;
            this.monto = monto;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombreTipoServicio() {
            return nombreTipoServicio;
        }

        public void setNombreTipoServicio(String nombreTipoServicio) {
            this.nombreTipoServicio = nombreTipoServicio;
        }

        public java.math.BigDecimal getMonto() {
            return monto;
        }

        public void setMonto(java.math.BigDecimal monto) {
            this.monto = monto;
        }

        @Override
        public String toString() {
            return "ServicioReciboDTO{" +
                    "id=" + id +
                    ", nombreTipoServicio='" + nombreTipoServicio + '\'' +
                    ", monto=" + monto +
                    '}';
        }
    }

    /**
     * DTO interna para representar datos del contrato en el recibo
     */
    public static class ContratoReciboDTO {
        private String fechaInicioContrato;
        private String tipoInmueble;

        public ContratoReciboDTO() {
        }

        public ContratoReciboDTO(String fechaInicioContrato, String tipoInmueble) {
            this.fechaInicioContrato = fechaInicioContrato;
            this.tipoInmueble = tipoInmueble;
        }

        public String getFechaInicioContrato() {
            return fechaInicioContrato;
        }

        public void setFechaInicioContrato(String fechaInicioContrato) {
            this.fechaInicioContrato = fechaInicioContrato;
        }

        public String getTipoInmueble() {
            return tipoInmueble;
        }

        public void setTipoInmueble(String tipoInmueble) {
            this.tipoInmueble = tipoInmueble;
        }

        @Override
        public String toString() {
            return "ContratoReciboDTO{" +
                    "fechaInicioContrato='" + fechaInicioContrato + '\'' +
                    ", tipoInmueble='" + tipoInmueble + '\'' +
                    '}';
        }
    }

    /**
     * DTO interna para representar datos de personas (propietario/inquilino) en el recibo
     */
    public static class PersonaReciboDTO {
        private String nombre;
        private String apellido;
        private String direccion;
        private String barrio;
        private String dni;

        public PersonaReciboDTO() {
        }

        public PersonaReciboDTO(String nombre, String apellido, String direccion, String barrio, String dni) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.direccion = direccion;
            this.barrio = barrio;
            this.dni = dni;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getBarrio() {
            return barrio;
        }

        public void setBarrio(String barrio) {
            this.barrio = barrio;
        }

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
        }

        @Override
        public String toString() {
            return "PersonaReciboDTO{" +
                    "nombre='" + nombre + '\'' +
                    ", apellido='" + apellido + '\'' +
                    ", direccion='" + direccion + '\'' +
                    ", barrio='" + barrio + '\'' +
                    ", dni='" + dni + '\'' +
                    '}';
        }
    }
}

