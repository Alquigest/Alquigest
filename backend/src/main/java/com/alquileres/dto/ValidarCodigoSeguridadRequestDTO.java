package com.alquileres.dto;

import jakarta.validation.constraints.NotBlank;

public class ValidarCodigoSeguridadRequestDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "El código de seguridad es obligatorio")
    private String codigo;

    public ValidarCodigoSeguridadRequestDTO() {}

    public ValidarCodigoSeguridadRequestDTO(String username, String codigo) {
        this.username = username;
        this.codigo = codigo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
