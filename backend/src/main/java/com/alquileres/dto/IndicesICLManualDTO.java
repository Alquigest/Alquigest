package com.alquileres.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * DTO para recibir índices ICL manuales cuando la API del BCRA falla
 */
public class IndicesICLManualDTO {

    @NotNull(message = "El índice inicial es obligatorio")
    @Positive(message = "El índice inicial debe ser positivo")
    private BigDecimal indiceInicial;

    @NotNull(message = "El índice final es obligatorio")
    @Positive(message = "El índice final debe ser positivo")
    private BigDecimal indiceFinal;

    public IndicesICLManualDTO() {
    }

    public IndicesICLManualDTO(BigDecimal indiceInicial, BigDecimal indiceFinal) {
        this.indiceInicial = indiceInicial;
        this.indiceFinal = indiceFinal;
    }

    public BigDecimal getIndiceInicial() {
        return indiceInicial;
    }

    public void setIndiceInicial(BigDecimal indiceInicial) {
        this.indiceInicial = indiceInicial;
    }

    public BigDecimal getIndiceFinal() {
        return indiceFinal;
    }

    public void setIndiceFinal(BigDecimal indiceFinal) {
        this.indiceFinal = indiceFinal;
    }

    /**
     * Calcula la tasa de aumento basándose en los índices
     * @return La tasa de aumento (indiceFinal / indiceInicial)
     */
    public BigDecimal calcularTasaAumento() {
        if (indiceInicial == null || indiceFinal == null || indiceInicial.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }
        return indiceFinal.divide(indiceInicial, 10, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String toString() {
        return "IndicesICLManualDTO{" +
                "indiceInicial=" + indiceInicial +
                ", indiceFinal=" + indiceFinal +
                '}';
    }
}

