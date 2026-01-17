package com.alquileres.dto;

import java.util.List;

public class CodigosSeguridadResponseDTO {
    private List<String> codigos;
    private String mensaje;

    public CodigosSeguridadResponseDTO() {}

    public CodigosSeguridadResponseDTO(List<String> codigos, String mensaje) {
        this.codigos = codigos;
        this.mensaje = mensaje;
    }

    public List<String> getCodigos() {
        return codigos;
    }

    public void setCodigos(List<String> codigos) {
        this.codigos = codigos;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
