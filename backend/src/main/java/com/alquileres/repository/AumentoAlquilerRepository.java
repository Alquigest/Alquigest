package com.alquileres.repository;

import com.alquileres.model.AumentoAlquiler;
import com.alquileres.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AumentoAlquilerRepository extends JpaRepository<AumentoAlquiler, Long> {

    // Buscar todos los aumentos de un contrato
    List<AumentoAlquiler> findByContrato(Contrato contrato);

    // Buscar todos los aumentos de un contrato ordenados por fecha descendente
    @Query("SELECT a FROM AumentoAlquiler a WHERE a.contrato = :contrato ORDER BY a.fechaAumento DESC")
    List<AumentoAlquiler> findByContratoOrderByFechaAumentoDesc(@Param("contrato") Contrato contrato);

    // Buscar todos los aumentos de un contrato por su ID
    @Query("SELECT a FROM AumentoAlquiler a WHERE a.contrato.id = :contratoId ORDER BY a.fechaAumento DESC")
    List<AumentoAlquiler> findByContratoIdOrderByFechaAumentoDesc(@Param("contratoId") Long contratoId);

    // Contar aumentos de un contrato
    Long countByContrato(Contrato contrato);

    // Buscar aumentos en un rango de fechas
    @Query("SELECT a FROM AumentoAlquiler a WHERE a.contrato.id = :contratoId AND a.fechaAumento >= :fechaInicio AND a.fechaAumento <= :fechaFin ORDER BY a.fechaAumento DESC")
    List<AumentoAlquiler> findByContratoIdAndFechaAumentoBetween(@Param("contratoId") Long contratoId, @Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin);
}

