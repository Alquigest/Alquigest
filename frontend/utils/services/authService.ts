import BACKEND_URL from "@/utils/backendURL";

/**
 * Interfaz para los permisos del usuario
 */
export interface UserPermissions {
  [key: string]: boolean;
}

/**
 * Interfaz para la respuesta del endpoint /auth/me
 */
export interface UserProfile {
  id: number;
  username: string;
  email: string;
  roles: string[];
  permisos: UserPermissions;
  tokenType: string;
  accessToken: string | null;
}

/**
 * Servicio centralizado para operaciones de autenticación
 * Responsabilidades:
 * - Obtener datos del perfil del usuario actual
 * - Manejar respuestas del servidor
 * - Centralizar URLs de auth
 */
export const AuthService = {
  /**
   * GET: Obtiene el perfil del usuario actual
   * @returns {Promise<UserProfile>} Perfil completo del usuario
   * @throws {Error} Si falla la conexión o el servidor retorna error
   */
  getCurrentProfile: async (): Promise<UserProfile> => {
    const response = await fetch(`${BACKEND_URL}/auth/me`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(
        errorData.message || "Error al obtener el perfil del usuario"
      );
    }

    const result: UserProfile = await response.json();
    return result;
  },
};
