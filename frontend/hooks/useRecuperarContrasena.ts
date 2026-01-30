import { useState } from "react";
import {
  RecuperarContrasenaService,
  ValidarCodigoResponse,
  ResetearContrasenaResponse,
} from "@/utils/services/recuperarContrasenaService";

/**
 * Estados del flujo de recuperación de contraseña
 */
type FlowStep = "validar-codigo" | "nueva-contrasena" | "completado";

/**
 * Hook custom para el flujo de recuperación de contraseña con códigos de un solo uso
 * 
 * @description
 * Maneja el flujo completo de recuperación de contraseña:
 * 1. Validación del código de seguridad
 * 2. Reseteo de la contraseña con el token obtenido
 * 
 * El token se mantiene en memoria durante el flujo
 * 
 * @example
 * ```tsx
 * const { 
 *   validarCodigo, 
 *   resetearContrasena, 
 *   loading, 
 *   error, 
 *   step,
 *   token 
 * } = useRecuperarContrasena();
 * 
 * // Paso 1: Validar código
 * const result = await validarCodigo({ username: "user", codigo: "ABC123" });
 * 
 * // Paso 2: Resetear contraseña
 * if (token) {
 *   await resetearContrasena({ 
 *     nuevaContrasena: "nueva123", 
 *     confirmarContrasena: "nueva123" 
 *   });
 * }
 * ```
 */
export function useRecuperarContrasena() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [step, setStep] = useState<FlowStep>("validar-codigo");
  const [token, setToken] = useState<string | null>(null);
  const [mensaje, setMensaje] = useState<string | null>(null);

  /**
   * Valida un código de seguridad y obtiene el token
   * @param {Object} data - Datos de validación
   * @param {string} data.username - Nombre de usuario
   * @param {string} data.codigo - Código de seguridad de un solo uso
   * @returns {Promise<ValidarCodigoResponse>} Respuesta de la validación
   */
  const validarCodigo = async (data: {
    username: string;
    codigo: string;
  }): Promise<ValidarCodigoResponse> => {
    setLoading(true);
    setError(null);
    setMensaje(null);

    try {
      const response = await RecuperarContrasenaService.validarCodigo(data);

      if (response.exitoso && response.token) {
        setToken(response.token);
        setStep("nueva-contrasena");
        setMensaje(response.mensaje);
      } else {
        setError(response.mensaje);
      }

      return response;
    } catch (err: any) {
      const errorMessage = err.message || "Error al validar el código";
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  /**
   * Resetea la contraseña usando el token obtenido previamente
   * @param {Object} data - Datos para el reseteo
   * @param {string} data.nuevaContrasena - Nueva contraseña
   * @param {string} data.confirmarContrasena - Confirmación de la nueva contraseña
   * @returns {Promise<ResetearContrasenaResponse>} Respuesta del reseteo
   * @throws {Error} Si no hay token disponible
   */
  const resetearContrasena = async (data: {
    nuevaContrasena: string;
    confirmarContrasena: string;
  }): Promise<ResetearContrasenaResponse> => {
    if (!token) {
      throw new Error("No hay token disponible. Debe validar el código primero.");
    }

    setLoading(true);
    setError(null);
    setMensaje(null);

    try {
      const response = await RecuperarContrasenaService.resetearContrasena({
        token,
        ...data,
      });

      setMensaje(response.message);
      setStep("completado");
      // Limpiar el token por seguridad
      setToken(null);

      return response;
    } catch (err: any) {
      const errorMessage = err.message || "Error al resetear la contraseña";
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  /**
   * Reinicia el flujo completo
   */
  const resetFlow = () => {
    setStep("validar-codigo");
    setToken(null);
    setError(null);
    setMensaje(null);
    setLoading(false);
  };

  return {
    // Estado
    loading,
    error,
    mensaje,
    step,
    token,

    // Métodos
    validarCodigo,
    resetearContrasena,
    resetFlow,
  };
}
