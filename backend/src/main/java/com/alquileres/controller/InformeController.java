package com.alquileres.controller;

import com.alquileres.dto.*;
import com.alquileres.service.InformeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para la generación de informes del sistema
 */
@RestController
@RequestMapping("/api/informes")
@Tag(name = "Informes", description = "API para generación de informes del sistema")
public class InformeController {

    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    /**
     * INFORME 1: Honorarios por inmueble del mes actual
     *
     * Servicios utilizados:
     * - Generación de reportes de honorarios mensuales
     * - Dashboard administrativo
     * - Análisis financiero mensual
     *
     * @return JSON con honorarios por inmueble y total del mes
     */
    @GetMapping("/honorarios")
    @Operation(summary = "Informe de honorarios por inmueble",
               description = "Genera un informe con los honorarios del mes actual agrupados por inmueble. " +
                           "Incluye información del contrato, propietario, inquilino y monto del alquiler. " +
                           "Solo incluye alquileres pagados de contratos vigentes.")
    public ResponseEntity<?> generarInformeHonorarios() {
        try {
            InformeHonorariosDTO informe = informeService.generarInformeHonorarios();
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar informe de honorarios: " + e.getMessage());
        }
    }

    /**
     * INFORME 2: Pagos de alquileres del mes actual
     *
     * Servicios utilizados:
     * - Reporte de ingresos mensuales
     * - Control de pagos de alquileres
     * - Seguimiento de morosidad
     * - Conciliación bancaria
     *
     * @return JSON con detalle de todos los alquileres del mes y total pagado
     */
    @GetMapping("/alquileres")
    @Operation(summary = "Informe de pagos de alquileres del mes",
               description = "Genera un informe con todos los alquileres del mes actual (pagados y pendientes). " +
                           "Incluye fecha de pago, monto, fecha de vencimiento y estado de pago. " +
                           "Solo incluye contratos vigentes.")
    public ResponseEntity<?> generarInformeAlquileres() {
        try {
            InformeAlquileresDTO informe = informeService.generarInformeAlquileres();
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar informe de alquileres: " + e.getMessage());
        }
    }

    /**
     * INFORME 3: Aumentos de alquiler de los últimos N meses
     *
     * Servicios utilizados:
     * - Reporte histórico de aumentos
     * - Análisis de ajustes de precios
     * - Auditoría de modificaciones de contratos
     * - Seguimiento de índices de actualización
     *
     * @param meses Cantidad de meses hacia atrás (opcional, por defecto 6)
     * @return JSON con aumentos agrupados por contrato del período especificado
     */
    @GetMapping("/aumentos")
    @Operation(summary = "Informe de aumentos de alquiler",
               description = "Genera un informe con todos los aumentos de alquiler aplicados en los últimos N meses. " +
                           "Los aumentos están agrupados por contrato e incluyen fecha, montos anteriores/nuevos y porcentaje. " +
                           "Se puede especificar la cantidad de meses (por defecto 6).")
    public ResponseEntity<?> generarInformeAumentos(
            @RequestParam(required = false, defaultValue = "6") Integer meses) {
        try {
            InformeAumentosDTO informe = informeService.generarInformeAumentos(meses);
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar informe de aumentos: " + e.getMessage());
        }
    }

    /**
     * INFORME 4: Pagos de servicios del mes actual con detalle completo
     *
     * Servicios utilizados:
     * - Reporte de gastos de servicios
     * - Control de expensas
     * - Conciliación de pagos de servicios
     * - Dashboard de servicios por contrato
     * - Análisis de costos operativos
     *
     * @return JSON con detalle completo de pagos de servicios incluyendo datos del contrato y alquiler relacionado
     */
    @GetMapping("/pagos-servicios")
    @Operation(summary = "Informe de pagos de servicios",
               description = "Genera un informe con todos los pagos de servicios del mes actual. " +
                           "Incluye fecha, monto, período, tipo de servicio, datos del contrato (partes y inmueble) " +
                           "y datos del alquiler relacionado del mismo período.")
    public ResponseEntity<?> generarInformePagosServicios() {
        try {
            InformePagosServiciosDTO informe = informeService.generarInformePagosServicios();
            return ResponseEntity.ok(informe);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al generar informe de pagos de servicios: " + e.getMessage());
        }
    }
}

