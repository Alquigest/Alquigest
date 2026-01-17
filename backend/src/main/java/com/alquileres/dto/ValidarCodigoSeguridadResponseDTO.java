package com.alquileres.dto;

public class ValidarCodigoSeguridadResponseDTO {
    private String token;
    private String mensaje;
    private boolean exitoso;

    public ValidarCodigoSeguridadResponseDTO() {}

    public ValidarCodigoSeguridadResponseDTO(String token, String mensaje, boolean exitoso) {
        this.token = token;
        this.mensaje = mensaje;
        this.exitoso = exitoso;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
}
