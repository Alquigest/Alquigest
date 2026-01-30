import BACKEND_URL from "@/utils/backendURL";

/**
 * Interfaz para la validación de código de seguridad
 */
export interface ValidarCodigoRequest {
  username: string;
  codigo: string;
}

/**
 * Interfaz para la respuesta de validación de código
 */
export interface ValidarCodigoResponse {
  token: string | null;
  mensaje: string;
  exitoso: boolean;
}

/**
 * Interfaz para el reseteo de contraseña
 */
export interface ResetearContrasenaRequest {
  token: string;
  nuevaContrasena: string;
  confirmarContrasena: string;
}

/**
 * Interfaz para la respuesta del reseteo de contraseña
 */
export interface ResetearContrasenaResponse {
  message: string;
}

/**
 * Servicio centralizado para operaciones de recuperación de contraseña
 * Responsabilidades:
 * - Abstraer URLs de la API
 * - Manejar transformaciones de datos
 * - Validar respuestas del backend
 */
export const RecuperarContrasenaService = {
  /**
   * POST: Valida un código de seguridad de un solo uso
   * @param {ValidarCodigoRequest} data - Username y código de seguridad
   * @returns {Promise<ValidarCodigoResponse>} Respuesta con token si es exitoso
   * @throws {Error} Si falla la conexión o la validación del servidor
   */
  validarCodigo: async (data: ValidarCodigoRequest): Promise<ValidarCodigoResponse> => {
    const response = await fetch(`${BACKEND_URL}/codigos-seguridad/validar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.mensaje || "Error al validar el código de seguridad");
    }

    const result: ValidarCodigoResponse = await response.json();
    return result;
  },

  /**
   * POST: Resetea la contraseña usando el token de validación
   * @param {ResetearContrasenaRequest} data - Token y nuevas contraseñas
   * @returns {Promise<ResetearContrasenaResponse>} Mensaje de confirmación
   * @throws {Error} Si falla la conexión o el reseteo
   */
  resetearContrasena: async (data: ResetearContrasenaRequest): Promise<ResetearContrasenaResponse> => {
    const response = await fetch(`${BACKEND_URL}/auth/resetear-contrasena`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || "Error al resetear la contraseña");
    }

    const result: ResetearContrasenaResponse = await response.json();
    return result;
  },
};
