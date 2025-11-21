package com.alquileres.service;

import com.alquileres.dto.*;
import com.alquileres.model.*;
import com.alquileres.repository.PagoServicioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la generación de recibos de servicios
 * Minimiza acoplamiento delegando a servicios en lugar de acceder directamente a repositorios
 */
@Service
public class ReciboServicioService {

    private static final Logger logger = LoggerFactory.getLogger(ReciboServicioService.class);

    private final PagoServicioRepository pagoServicioRepository;
    private final ContratoService contratoService;
    private final InmuebleService inmuebleService;
    private final PropietarioService propietarioService;
    private final InquilinoService inquilinoService;

    public ReciboServicioService(
            PagoServicioRepository pagoServicioRepository,
            ContratoService contratoService,
            InmuebleService inmuebleService,
            PropietarioService propietarioService,
            InquilinoService inquilinoService) {
        this.pagoServicioRepository = pagoServicioRepository;
        this.contratoService = contratoService;
        this.inmuebleService = inmuebleService;
        this.propietarioService = propietarioService;
        this.inquilinoService = inquilinoService;
    }

    /**
     * Genera un recibo de servicios para un período y contrato específico
     * Incluye TODOS los servicios que han sido pagados en ese período para ese contrato
     *
     * @param contratoId ID del contrato
     * @param periodo Período en formato mm/aaaa (ej: 11/2025)
     * @return ReciboServicioDTO con todos los datos del recibo
     */
    @Transactional(readOnly = true)
    public ReciboServicioDTO generarRecibo(Long contratoId, String periodo) {
        logger.info("Iniciando generación de recibo para contrato ID: {} y período: {}", contratoId, periodo);

        // Obtener la entidad Contrato (no el DTO) para acceder a sus relaciones
        Contrato contrato = contratoService.obtenerContratoEntidadPorId(contratoId);
        logger.debug("Contrato obtenido - ID: {}", contrato.getId());

        // Obtener TODOS los pagos pagados del período para este contrato
        List<PagoServicio> pagosPagadosDelPeriodo = pagoServicioRepository
                .findByPeriodoAndServicioContratoContratoId(periodo, contratoId)
                .stream()
                .filter(p -> Boolean.TRUE.equals(p.getEstaPagado()))
                .toList();

        logger.debug("Se encontraron {} pagos PAGADOS para el período {} del contrato ID: {}",
                   pagosPagadosDelPeriodo.size(), periodo, contratoId);

        if (pagosPagadosDelPeriodo.isEmpty()) {
            logger.warn("No hay pagos pagados para el contrato {} en el período {}", contratoId, periodo);
        }

        // Construir lista de servicios del recibo (sin duplicados)
        List<ReciboServicioDTO.ServicioReciboDTO> servicios = pagosPagadosDelPeriodo.stream()
                .map(p -> {
                    ServicioContrato sc = p.getServicioContrato();
                    return new ReciboServicioDTO.ServicioReciboDTO(
                            sc.getId(),
                            sc.getTipoServicio().getNombre(),
                            p.getMonto()
                    );
                })
                .distinct()
                .collect(Collectors.toList());

        logger.debug("Se construyeron {} servicios únicos para el recibo", servicios.size());

        // Obtener datos del inmueble a través de InmuebleService
        InmuebleDTO inmuebleDTO = inmuebleService.obtenerInmueblePorId(contrato.getInmueble().getId());

        // Construir datos del contrato
        String tipoInmuebleNombre = inmuebleDTO.getTipoInmuebleNombre() != null ?
                inmuebleDTO.getTipoInmuebleNombre() : "N/A";
        ReciboServicioDTO.ContratoReciboDTO datosContrato = new ReciboServicioDTO.ContratoReciboDTO(
                contrato.getFechaInicio(),
                tipoInmuebleNombre
        );

        logger.debug("Datos del contrato construidos - Fecha inicio: {}, Tipo inmueble: {}",
                   contrato.getFechaInicio(), tipoInmuebleNombre);

        // Obtener datos del propietario a través de PropietarioService
        PropietarioDTO propietarioDTO = propietarioService.obtenerPropietarioPorId(
                contrato.getInmueble().getPropietarioId());

        ReciboServicioDTO.PersonaReciboDTO datosPropietario = new ReciboServicioDTO.PersonaReciboDTO(
                propietarioDTO.getNombre(),
                propietarioDTO.getApellido(),
                propietarioDTO.getDireccion(),
                propietarioDTO.getBarrio(),
                propietarioDTO.getCuil()
        );

        logger.debug("Datos del propietario construidos: {} {}",
                   propietarioDTO.getNombre(), propietarioDTO.getApellido());

        // Obtener datos del inquilino a través de InquilinoService
        InquilinoDTO inquilinoDTO = inquilinoService.obtenerInquilinoPorId(
                contrato.getInquilino().getId());

        ReciboServicioDTO.PersonaReciboDTO datosInquilino = new ReciboServicioDTO.PersonaReciboDTO(
                inquilinoDTO.getNombre(),
                inquilinoDTO.getApellido(),
                inmuebleDTO.getDireccion(), // Dirección del inmueble alquilado
                inquilinoDTO.getBarrio(),
                inquilinoDTO.getCuil()
        );

        logger.debug("Datos del inquilino construidos: {} {}",
                   inquilinoDTO.getNombre(), inquilinoDTO.getApellido());

        // Construir y retornar el recibo
        ReciboServicioDTO recibo = new ReciboServicioDTO(
                periodo,
                servicios,
                datosContrato,
                datosPropietario,
                datosInquilino
        );

        logger.info("Recibo generado exitosamente para período: {} y contrato ID: {} con {} servicios pagados",
                   periodo, contratoId, servicios.size());
        return recibo;
    }
}



