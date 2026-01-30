import { useState } from "react";
import {
  CodigosSeguridadService,
  CodigosDisponiblesResponse,
  CodigosRegeneradosResponse,
} from "@/utils/services/codigosSeguridadService";

/**
 * Hook custom para gestionar códigos de seguridad
 * 
 * @description
 * Maneja la consulta y regeneración de códigos de seguridad
 * 
 * @example
 * ```tsx
 * const { checkDisponibles, regenerarCodigos, loading, error } = useCodigosSeguridad();
 * ```
 */
export function useCodigosSeguridad() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [codigosInfo, setCodigosInfo] =
    useState<CodigosDisponiblesResponse | null>(null);
  const [codigosRegenerados, setCodigosRegenerados] =
    useState<CodigosRegeneradosResponse | null>(null);

  /**
   * Verifica la cantidad de códigos de seguridad disponibles
   * @returns {Promise<CodigosDisponiblesResponse | null>} Información de códigos o null si falla
   */
  const checkDisponibles = async (): Promise<CodigosDisponiblesResponse | null> => {
    setLoading(true);
    setError(null);
    setCodigosInfo(null);

    try {
      const result = await CodigosSeguridadService.getDisponibles();
      setCodigosInfo(result);
      return result;
    } catch (err: any) {
      const errorMessage =
        err.message || "Error al verificar códigos disponibles";
      setError(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  /**
   * Regenera los códigos de seguridad del usuario
   * @returns {Promise<CodigosRegeneradosResponse | null>} Nuevos códigos o null si falla
   */
  const regenerarCodigos = async (): Promise<CodigosRegeneradosResponse | null> => {
    setLoading(true);
    setError(null);
    setCodigosRegenerados(null);

    try {
      const result = await CodigosSeguridadService.regenerar();
      setCodigosRegenerados(result);
      return result;
    } catch (err: any) {
      const errorMessage =
        err.message || "Error al regenerar los códigos de seguridad";
      setError(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  return {
    checkDisponibles,
    regenerarCodigos,
    loading,
    error,
    codigosInfo,
    codigosRegenerados,
  };
}
