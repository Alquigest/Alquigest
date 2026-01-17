package com.alquileres.dto;

public class CodigosDisponiblesResponseDTO {
    private Long cantidad;
    private String mensaje;

    public CodigosDisponiblesResponseDTO() {}

    public CodigosDisponiblesResponseDTO(Long cantidad, String mensaje) {
        this.cantidad = cantidad;
        this.mensaje = mensaje;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
