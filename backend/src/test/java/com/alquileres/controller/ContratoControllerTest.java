package com.alquileres.controller;

import com.alquileres.dto.ContratoDTO;
import com.alquileres.dto.ContratoCreateDTO;
import com.alquileres.dto.EstadoContratoUpdateDTO;
import com.alquileres.service.ContratoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratoControllerTest {

    @Mock
    private ContratoService contratoService;

    @InjectMocks
    private ContratoController contratoController;

    @Test
    void obtenerTodosLosContratos_returnsListOfContratos_whenContratosExist() {
        List<ContratoDTO> contratos = List.of(
            createContratoDTO(1L, 1L, 1L),
            createContratoDTO(2L, 2L, 2L)
        );
        when(contratoService.obtenerTodosLosContratos()).thenReturn(contratos);

        ResponseEntity<List<ContratoDTO>> response = contratoController.obtenerTodosLosContratos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contratos, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(contratoService).obtenerTodosLosContratos();
    }

    @Test
    void obtenerTodosLosContratos_returnsEmptyList_whenNoContratosExist() {
        when(contratoService.obtenerTodosLosContratos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ContratoDTO>> response = contratoController.obtenerTodosLosContratos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(contratoService).obtenerTodosLosContratos();
    }

    @Test
    void obtenerContratoPorId_returnsContrato_whenValidIdProvided() {
        Long id = 1L;
        ContratoDTO contrato = createContratoDTO(id, 1L, 1L);
        when(contratoService.obtenerContratoPorId(id)).thenReturn(contrato);

        ResponseEntity<ContratoDTO> response = contratoController.obtenerContratoPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contrato, response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void obtenerContratoPorId_throwsException_whenContratoNotFound() {
        Long id = 999L;
        when(contratoService.obtenerContratoPorId(id))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato no encontrado"));

        assertThrows(ResponseStatusException.class, () -> contratoController.obtenerContratoPorId(id));
    }

    @Test
    void obtenerContratosPorInmueble_returnsContratos_whenValidInmuebleIdProvided() {
        Long inmuebleId = 1L;
        List<ContratoDTO> contratos = List.of(createContratoDTO(1L, inmuebleId, 1L));
        when(contratoService.obtenerContratosPorInmueble(inmuebleId)).thenReturn(contratos);

        ResponseEntity<List<ContratoDTO>> response = contratoController.obtenerContratosPorInmueble(inmuebleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contratos, response.getBody());
        verify(contratoService).obtenerContratosPorInmueble(inmuebleId);
    }

    @Test
    void obtenerContratosPorInquilino_returnsContratos_whenValidInquilinoIdProvided() {
        Long inquilinoId = 1L;
        List<ContratoDTO> contratos = List.of(createContratoDTO(1L, 1L, inquilinoId));
        when(contratoService.obtenerContratosPorInquilino(inquilinoId)).thenReturn(contratos);

        ResponseEntity<List<ContratoDTO>> response = contratoController.obtenerContratosPorInquilino(inquilinoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contratos, response.getBody());
        verify(contratoService).obtenerContratosPorInquilino(inquilinoId);
    }

    @Test
    void obtenerContratosVigentes_returnsActiveContratos() {
        List<ContratoDTO> contratos = List.of(createContratoDTO(1L, 1L, 1L));
        when(contratoService.obtenerContratosVigentes()).thenReturn(contratos);

        ResponseEntity<List<ContratoDTO>> response = contratoController.obtenerContratosVigentes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contratos, response.getBody());
    }

    @Test
    void obtenerContratosNoVigentes_returnsInactiveContratos() {
        List<ContratoDTO> contratos = List.of(createContratoDTO(1L, 1L, 1L));
        when(contratoService.obtenerContratosNoVigentes()).thenReturn(contratos);

        ResponseEntity<List<ContratoDTO>> response = contratoController.obtenerContratosNoVigentes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contratos, response.getBody());
    }

    @Test
    void contarContratosVigentes_returnsCount() {
        Long count = 10L;
        when(contratoService.contarContratosVigentes()).thenReturn(count);

        ResponseEntity<Long> response = contratoController.contarContratosVigentes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(count, response.getBody());
    }

    @Test
    void obtenerContratosProximosAVencer_returnsContratosAboutToExpire() {
        int diasAntes = 30;
        List<ContratoDTO> contratos = List.of(createContratoDTO(1L, 1L, 1L));
        when(contratoService.obtenerContratosProximosAVencer(diasAntes)).thenReturn(contratos);

        ResponseEntity<List<ContratoDTO>> response = contratoController.obtenerContratosProximosAVencer(diasAntes);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contratos, response.getBody());
        verify(contratoService).obtenerContratosProximosAVencer(diasAntes);
    }

    @Test
    void contarContratosProximosAVencer_returnsCount() {
        int diasAntes = 30;
        Long count = 5L;
        when(contratoService.contarContratosProximosAVencer(diasAntes)).thenReturn(count);

        ResponseEntity<Long> response = contratoController.contarContratosProximosAVencer(diasAntes);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(count, response.getBody());
        verify(contratoService).contarContratosProximosAVencer(diasAntes);
    }

    @Test
    void crearContrato_returnsCreatedContrato_whenValidDataProvided() {
        ContratoCreateDTO inputDTO = createContratoCreateDTO(1L, 1L);
        ContratoDTO createdDTO = createContratoDTO(1L, 1L, 1L);
        when(contratoService.crearContrato(inputDTO)).thenReturn(createdDTO);

        ResponseEntity<ContratoDTO> response = contratoController.crearContrato(inputDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdDTO, response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void terminarContrato_returnsUpdatedContrato_whenValidDataProvided() {
        Long id = 1L;
        EstadoContratoUpdateDTO estadoDTO = new EstadoContratoUpdateDTO();
        estadoDTO.setEstadoContratoId(2);
        
        ContratoDTO updatedDTO = createContratoDTO(id, 1L, 1L);
        when(contratoService.terminarContrato(id, estadoDTO)).thenReturn(updatedDTO);

        ResponseEntity<ContratoDTO> response = contratoController.terminarContrato(id, estadoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDTO, response.getBody());
    }

    @Test
    void existeContrato_returnsTrue_whenContratoExists() {
        Long id = 1L;
        when(contratoService.existeContrato(id)).thenReturn(true);

        ResponseEntity<Boolean> response = contratoController.existeContrato(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void existeContrato_returnsFalse_whenContratoDoesNotExist() {
        Long id = 999L;
        when(contratoService.existeContrato(id)).thenReturn(false);

        ResponseEntity<Boolean> response = contratoController.existeContrato(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void cargarPdf_returnsSuccess_whenValidPdfProvided() throws Exception {
        Long id = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "contrato.pdf",
            "application/pdf",
            "Test PDF content".getBytes()
        );
        
        ContratoDTO updatedDTO = createContratoDTO(id, 1L, 1L);
        when(contratoService.guardarPdf(eq(id), any(byte[].class), anyString())).thenReturn(updatedDTO);

        ResponseEntity<?> response = contratoController.cargarPdf(id, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("PDF cargado exitosamente", body.get("mensaje"));
    }

    @Test
    void cargarPdf_returnsBadRequest_whenNotPdfFile() {
        Long id = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "documento.txt",
            "text/plain",
            "Test content".getBytes()
        );

        ResponseEntity<?> response = contratoController.cargarPdf(id, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void cargarPdf_returnsBadRequest_whenFileTooLarge() {
        Long id = 1L;
        byte[] largeContent = new byte[11 * 1024 * 1024]; // 11MB
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "contrato.pdf",
            "application/pdf",
            largeContent
        );

        ResponseEntity<?> response = contratoController.cargarPdf(id, file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void descargarPdf_returnsPdf_whenPdfExists() {
        Long id = 1L;
        byte[] pdfBytes = "Test PDF content".getBytes();
        when(contratoService.obtenerPdf(id)).thenReturn(pdfBytes);

        ResponseEntity<?> response = contratoController.descargarPdf(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pdfBytes, response.getBody());
        assertEquals("application/pdf", response.getHeaders().getFirst("Content-Type"));
    }

    @Test
    void descargarPdf_returnsNotFound_whenNoPdfExists() {
        Long id = 1L;
        when(contratoService.obtenerPdf(id)).thenReturn(null);

        ResponseEntity<?> response = contratoController.descargarPdf(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void inmuebleTieneContratoVigente_returnsTrue_whenContratoExists() {
        Long inmuebleId = 1L;
        when(contratoService.inmuebleTieneContratoVigente(inmuebleId)).thenReturn(true);

        ResponseEntity<Boolean> response = contratoController.inmuebleTieneContratoVigente(inmuebleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void inmuebleTieneContratoVigente_returnsFalse_whenNoContratoExists() {
        Long inmuebleId = 1L;
        when(contratoService.inmuebleTieneContratoVigente(inmuebleId)).thenReturn(false);

        ResponseEntity<Boolean> response = contratoController.inmuebleTieneContratoVigente(inmuebleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    private ContratoDTO createContratoDTO(Long id, Long inmuebleId, Long inquilinoId) {
        ContratoDTO dto = new ContratoDTO();
        dto.setId(id);
        dto.setInmuebleId(inmuebleId);
        dto.setInquilinoId(inquilinoId);
        dto.setMonto(new BigDecimal("50000"));
        dto.setFechaInicio(LocalDate.of(2024, 1, 1).toString());
        dto.setFechaFin(LocalDate.of(2025, 1, 1).toString());
        dto.setEstadoContratoId(1);
        return dto;
    }

    private ContratoCreateDTO createContratoCreateDTO(Long inmuebleId, Long inquilinoId) {
        ContratoCreateDTO dto = new ContratoCreateDTO();
        dto.setInmuebleId(inmuebleId);
        dto.setInquilinoId(inquilinoId);
        dto.setMonto(new BigDecimal("50000"));
        dto.setFechaInicio(LocalDate.of(2024, 1, 1).toString());
        dto.setFechaFin(LocalDate.of(2025, 1, 1).toString());
        dto.setEstadoContratoId(1);
        return dto;
    }
}
