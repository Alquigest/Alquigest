package com.alquileres.dto;

import jakarta.validation.constraints.NotBlank;

public class RevelarClaveFiscalRequest {

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public RevelarClaveFiscalRequest() {
    }

    public RevelarClaveFiscalRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

