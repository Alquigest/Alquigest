package com.alquileres.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio centralizado para manejo de fechas del sistema.
 * Permite configurar fechas específicas para testing y facilita las pruebas
 * de procesos automáticos que dependen de la fecha actual.
 */
@Service
public class ClockService {

    private LocalDate overrideDate = null;

    /**
     * Obtiene la fecha actual del sistema o la fecha configurada manualmente para testing.
     *
     * @return La fecha configurada o la fecha real del sistema
     */
    public LocalDate getCurrentDate() {
        return overrideDate != null ? overrideDate : LocalDate.now();
    }

    /**
     * Obtiene el LocalDateTime actual del sistema o basado en la fecha configurada.
     * Si hay una fecha configurada, retorna el inicio del día (00:00:00).
     *
     * @return El LocalDateTime configurado o el real del sistema
     */
    public LocalDateTime getCurrentDateTime() {
        return overrideDate != null
            ? overrideDate.atStartOfDay()
            : LocalDateTime.now();
    }

    /**
     * Configura una fecha específica para testing.
     * Todos los métodos que usen este servicio devolverán esta fecha.
     *
     * @param date La fecha a configurar
     */
    public void setOverrideDate(LocalDate date) {
        this.overrideDate = date;
    }

    /**
     * Limpia la fecha configurada y vuelve al comportamiento normal del sistema.
     * Después de llamar este método, getCurrentDate() devolverá LocalDate.now()
     */
    public void clearOverride() {
        this.overrideDate = null;
    }

    /**
     * Verifica si actualmente hay una fecha configurada para testing.
     *
     * @return true si hay una fecha configurada, false si usa la fecha real
     */
    public boolean isDateOverridden() {
        return overrideDate != null;
    }

    /**
     * Obtiene la fecha configurada si existe.
     *
     * @return Optional con la fecha configurada, o vacío si no hay override
     */
    public Optional<LocalDate> getOverrideDate() {
        return Optional.ofNullable(overrideDate);
    }

    /**
     * Obtiene el año actual según la fecha del sistema o configurada.
     *
     * @return El año actual
     */
    public int getCurrentYear() {
        return getCurrentDate().getYear();
    }

    /**
     * Obtiene el mes actual según la fecha del sistema o configurada.
     *
     * @return El mes actual (1-12)
     */
    public int getCurrentMonth() {
        return getCurrentDate().getMonthValue();
    }

    /**
     * Obtiene el día del mes actual según la fecha del sistema o configurada.
     *
     * @return El día del mes (1-31)
     */
    public int getCurrentDayOfMonth() {
        return getCurrentDate().getDayOfMonth();
    }
}

