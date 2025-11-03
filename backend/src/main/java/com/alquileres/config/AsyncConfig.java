package com.alquileres.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

/**
 * Configuración para habilitar procesamiento asíncrono en la aplicación.
 * Utilizado principalmente para operaciones que no deben bloquear el hilo principal,
 * como el envío de emails.
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * Configura el executor para tareas asíncronas
     *
     * @return Executor configurado para manejar tareas asíncronas
     */
    @Bean(name = "taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Configuración del pool de hilos
        executor.setCorePoolSize(2);           // Hilos mínimos en el pool
        executor.setMaxPoolSize(5);            // Hilos máximos en el pool
        executor.setQueueCapacity(100);        // Capacidad de la cola de tareas
        executor.setThreadNamePrefix("async-"); // Prefijo para identificar hilos
        executor.setWaitForTasksToCompleteOnShutdown(true); // Esperar a que terminen las tareas al cerrar
        executor.setAwaitTerminationSeconds(60); // Tiempo máximo de espera al cerrar

        executor.initialize();
        return executor;
    }
}

