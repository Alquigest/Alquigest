package com.alquileres.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio de email utilizando la API oficial de Resend.
 * Más confiable y mejor soportado que SMTP para producción en Render.
 */
@Service
public class ResendEmailService {

    private static final Logger logger = LoggerFactory.getLogger(ResendEmailService.class);

    private final Resend resend;
    private final String fromEmail;

    public ResendEmailService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from-email:noreply@resend.dev}") String fromEmail) {
        this.resend = new Resend(apiKey);
        this.fromEmail = fromEmail;
        logger.info("ResendEmailService inicializado con email: {}", fromEmail);
    }

    /**
     * Envía un email de recuperación de contraseña
     *
     * @param destinatario Email del destinatario
     * @param usuario Nombre del usuario
     * @param token Token de recuperación
     */
    public void enviarEmailRecuperacionContrasena(String destinatario, String usuario, String token) {
        try {
            String enlace = "https://alquigest.onrender.com/auth/nueva-contrasena?token=" + token;
            String htmlContent = buildHtmlRecuperacionContrasena(usuario, enlace);

            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(destinatario)
                    .subject("Recuperación de Contraseña - Alquigest")
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(sendEmailRequest);

            if (response != null && response.getId() != null) {
                logger.info("Email de recuperación de contraseña enviado exitosamente a: {} (ID: {})",
                    destinatario, response.getId());
            } else {
                logger.warn("Email enviado pero sin confirmación de ID para: {}", destinatario);
            }
        } catch (ResendException e) {
            logger.error("Error de Resend al enviar email de recuperación a {}: {}",
                destinatario, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email de recuperación: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al enviar email de recuperación a {}: {}",
                destinatario, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email de recuperación: " + e.getMessage(), e);
        }
    }

    /**
     * Envía un email de prueba
     *
     * @param destinatario Email del destinatario
     */
    public void enviarEmailPrueba(String destinatario) {
        try {
            String htmlContent = buildHtmlPrueba();

            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(destinatario)
                    .subject("Email de Prueba - Alquigest")
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(sendEmailRequest);

            if (response != null && response.getId() != null) {
                logger.info("Email de prueba enviado exitosamente a: {} (ID: {})",
                    destinatario, response.getId());
            } else {
                logger.warn("Email de prueba enviado pero sin confirmación de ID para: {}", destinatario);
            }
        } catch (ResendException e) {
            logger.error("Error de Resend al enviar email de prueba a {}: {}",
                destinatario, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email de prueba: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al enviar email de prueba a {}: {}",
                destinatario, e.getMessage(), e);
            throw new RuntimeException("Error al enviar email de prueba: " + e.getMessage(), e);
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

