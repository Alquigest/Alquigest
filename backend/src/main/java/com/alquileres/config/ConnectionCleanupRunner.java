package com.alquileres.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Limpia conexiones idle en transacción de Supabase al iniciar la aplicación.
 * Esto previene que conexiones abiertas pero no committeadas bloqueen operaciones.
 */
@Component
public class ConnectionCleanupRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionCleanupRunner.class);
    private final DataSource dataSource;

    public ConnectionCleanupRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT pg_terminate_backend(pid) FROM pg_stat_activity " +
                        "WHERE state LIKE 'idle in transaction%' AND usename = 'postgres';";

            boolean result = statement.execute(sql);

            if (result) {
                logger.info(" - Conexiones idle en transaccion limpiadas al iniciar la aplicacion");
            } else {
                logger.info(" - No habia conexiones idle en transaccion para limpiar");
            }

        } catch (Exception e) {
            logger.warn("⚠️ Error al limpiar conexiones idle al iniciar: {}", e.getMessage());
            // No lanzar la excepción para no interrumpir el startup
        }
    }
}

