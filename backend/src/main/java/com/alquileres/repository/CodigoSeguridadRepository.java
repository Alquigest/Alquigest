package com.alquileres.repository;

import com.alquileres.model.CodigoSeguridad;
import com.alquileres.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CodigoSeguridadRepository extends JpaRepository<CodigoSeguridad, Long> {

    List<CodigoSeguridad> findByUsuarioAndUsadoFalse(Usuario usuario);

    @Query("SELECT COUNT(c) FROM CodigoSeguridad c WHERE c.usuario = :usuario AND c.usado = false")
    Long countCodigosDisponibles(@Param("usuario") Usuario usuario);

    @Modifying
    @Query("UPDATE CodigoSeguridad c SET c.usado = true, c.fechaUso = :fechaUso WHERE c.usuario = :usuario AND c.usado = false")
    void invalidarCodigosPorUsuario(@Param("usuario") Usuario usuario, @Param("fechaUso") LocalDateTime fechaUso);


    boolean existsByUsuarioAndUsadoFalse(Usuario usuario);
}
