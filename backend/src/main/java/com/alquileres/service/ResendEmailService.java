package com.alquileres.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio de email utilizando la API REST de Resend.
 * Más confiable y mejor soportado que SMTP para producción en Render.
 */
@Service
public class ResendEmailService {

    private static final Logger logger = LoggerFactory.getLogger(ResendEmailService.class);
    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    private final String apiKey;
    private final String fromEmail;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ResendEmailService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from-email:noreply@resend.dev}") String fromEmail,
            WebClient.Builder webClientBuilder) {
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();

        // Validar API Key
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.error("❌ RESEND_API_KEY NO ESTÁ CONFIGURADA - El servicio de email NO funcionará");
            logger.error("❌ Configura la variable de entorno: RESEND_API_KEY=re_tu_api_key");
        } else if (!apiKey.startsWith("re_")) {
            logger.warn("⚠️ RESEND_API_KEY no tiene el formato esperado (debería empezar con 're_')");
            logger.warn("⚠️ API Key actual: {}...", apiKey.substring(0, Math.min(5, apiKey.length())));
        } else {
            logger.info("✅ ResendEmailService inicializado correctamente");
            logger.info("   Email remitente: {}", fromEmail);
            logger.info("   API Key configurada: {}...{}",
                apiKey.substring(0, 6),
                apiKey.substring(apiKey.length() - 4));
        }
    }

    /**
     * Envía un email de recuperación de contraseña
     */
    public void enviarEmailRecuperacionContrasena(String destinatario, String usuario, String token) {
        try {
            String enlace = "https://alquigest.onrender.com/auth/nueva-contrasena?token=" + token;
            String htmlContent = buildHtmlRecuperacionContrasena(usuario, enlace);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("from", fromEmail);
            requestBody.put("to", new String[]{destinatario}); // Resend requiere array
            requestBody.put("subject", "Recuperación de Contraseña - Alquigest");
            requestBody.put("html", htmlContent);

            sendEmailViaResendAPI(requestBody);

            logger.info("Email de recuperación de contraseña enviado exitosamente a: {}",
                destinatario);
        } catch (Exception e) {
            logger.error("Error al enviar email de recuperación a {}: {}",
                destinatario, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email de recuperación: " + e.getMessage(), e);
        }
    }

    /**
     * Envía un email de prueba
     */
    public void enviarEmailPrueba(String destinatario) {
        try {
            String htmlContent = buildHtmlPrueba();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("from", fromEmail);
            requestBody.put("to", new String[]{destinatario}); // Resend requiere array
            requestBody.put("subject", "Email de Prueba - Alquigest");
            requestBody.put("html", htmlContent);

            sendEmailViaResendAPI(requestBody);

            logger.info("Email de prueba enviado exitosamente a: {}",
                destinatario);
        } catch (Exception e) {
            logger.error("Error al enviar email de prueba a {}: {}",
                destinatario, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email de prueba: " + e.getMessage(), e);
        }
    }

    /**
     * Envía email a través de la API REST de Resend
     */
    private void sendEmailViaResendAPI(Map<String, Object> requestBody) {
        try {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                logger.error("❌ RESEND_API_KEY no está configurada");
                throw new RuntimeException("RESEND_API_KEY no está configurada. Configura la variable de entorno en Render.");
            }

            logger.debug("Enviando email a Resend API...");
            logger.debug("Request body: {}", requestBody);

            String response = webClient.post()
                    .uri(RESEND_API_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(
                        status -> status.value() == 403,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                            .map(errorBody -> {
                                logger.error("❌ Resend API retornó 403 Forbidden");
                                logger.error("❌ Response body: {}", errorBody);
                                logger.error("❌ API Key usado: {}...{}",
                                    apiKey.substring(0, Math.min(10, apiKey.length())),
                                    apiKey.substring(Math.max(0, apiKey.length() - 4)));
                                return new RuntimeException("Error 403: " + errorBody);
                            })
                    )
                    .bodyToMono(String.class)
                    .block();

            logger.debug("Respuesta de Resend: {}", response);

            if (response != null && response.contains("id")) {
                logger.info("✅ Email enviado exitosamente a través de Resend API");
            } else {
                logger.warn("⚠️ Email enviado pero respuesta inesperada: {}", response);
            }
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException.Forbidden e) {
            String errorBody = e.getResponseBodyAsString();
            logger.error("❌ ERROR 403 FORBIDDEN de Resend API");
            logger.error("❌ Response body: {}", errorBody);
            logger.error("❌ Posibles causas:");
            logger.error("   1. Domain not verified");
            logger.error("   2. From email incorrect");
            logger.error("   3. API Key sin permisos correctos");
            logger.error("❌ From email usado: {}", requestBody.get("from"));
            logger.error("❌ API Key: {}...", apiKey.substring(0, Math.min(10, apiKey.length())));

            throw new RuntimeException("Error 403 de Resend: " + errorBody, e);
        } catch (WebClientException e) {
            logger.error("❌ Error de conexión con Resend API: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con Resend API: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("❌ Error inesperado al enviar email: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }

    /**
     * Construye el contenido HTML para el email de recuperación de contraseña
     */
    private String buildHtmlRecuperacionContrasena(String usuario, String enlace) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #007bff; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 20px; border-radius: 0 0 5px 5px; border: 1px solid #e0e0e0; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { font-size: 12px; color: #666; text-align: center; margin-top: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<h1>Alquigest</h1>" +
                "</div>" +
                "<div class=\"content\">" +
                "<p>Hola " + usuario + ",</p>" +
                "<p>Ha solicitado recuperar su contraseña. Por favor haga clic en el siguiente enlace para establecer una nueva contraseña:</p>" +
                "<a href=\"" + enlace + "\" class=\"button\">Recuperar Contraseña</a>" +
                "<p><strong>Este enlace expirará en 1 hora.</strong></p>" +
                "<p>Si no solicitó esta recuperación, ignore este email. Su contraseña permanecerá segura.</p>" +
                "<p>Saludos,<br>El equipo de Alquigest</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>&copy; 2026 Alquigest. Todos los derechos reservados.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Construye el contenido HTML para el email de prueba
     */
    private String buildHtmlPrueba() {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #28a745; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                ".content { background-color: #f9f9f9; padding: 20px; border-radius: 0 0 5px 5px; border: 1px solid #e0e0e0; }" +
                ".footer { font-size: 12px; color: #666; text-align: center; margin-top: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<div class=\"header\">" +
                "<h1>Alquigest - Email de Prueba</h1>" +
                "</div>" +
                "<div class=\"content\">" +
                "<p>Este es un email de prueba de la plataforma Alquigest.</p>" +
                "<p>Si está leyendo este mensaje, ¡el servicio de email está funcionando correctamente!</p>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>&copy; 2026 Alquigest. Todos los derechos reservados.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

