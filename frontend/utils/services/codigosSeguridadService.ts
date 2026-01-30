import BACKEND_URL from "@/utils/backendURL";

/**
 * Interfaz para la respuesta de códigos disponibles
 */
export interface CodigosDisponiblesResponse {
  cantidad: number;
  mensaje: string;
}

/**
 * Interfaz para la respuesta de regeneración de códigos
 */
export interface CodigosRegeneradosResponse {
  codigos: string[];
  mensaje: string;
}

/**
 * Servicio centralizado para operaciones de códigos de seguridad
 * Responsabilidades:
 * - Obtener cantidad de códigos disponibles
 * - Generar nuevos códigos de seguridad
 * - Manejar respuestas del servidor
 */
export const CodigosSeguridadService = {
  /**
   * GET: Obtiene la cantidad de códigos de seguridad disponibles del usuario
   * @returns {Promise<CodigosDisponiblesResponse>} Cantidad y mensaje
   * @throws {Error} Si falla la conexión o el servidor retorna error
   */
  getDisponibles: async (): Promise<CodigosDisponiblesResponse> => {
    const response = await fetch(`${BACKEND_URL}/codigos-seguridad/disponibles`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(
        errorData.mensaje || "Error al obtener los códigos disponibles"
      );
    }

    const result: CodigosDisponiblesResponse = await response.json();
    return result;
  },

  /**
   * POST: Regenera los códigos de seguridad del usuario
   * @returns {Promise<CodigosRegeneradosResponse>} Nuevos códigos y mensaje
   * @throws {Error} Si falla la conexión o el servidor retorna error
   */
  regenerar: async (): Promise<CodigosRegeneradosResponse> => {
    const response = await fetch(`${BACKEND_URL}/codigos-seguridad/regenerar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(
        errorData.mensaje || "Error al regenerar los códigos de seguridad"
      );
    }

    const result: CodigosRegeneradosResponse = await response.json();
    return result;
  },
};
