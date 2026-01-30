import { useState } from "react";
import { AuthService, UserProfile } from "@/utils/services/authService";

/**
 * Hook custom para obtener y gestionar el perfil del usuario actual
 * 
 * @description
 * Maneja la obtención del perfil del usuario desde el endpoint /auth/me
 * 
 * @example
 * ```tsx
 * const { profile, loading, error, fetchProfile } = useUserProfile();
 * 
 * useEffect(() => {
 *   fetchProfile();
 * }, []);
 * ```
 */
export function useUserProfile() {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  /**
   * Obtiene el perfil del usuario actual
   * @returns {Promise<UserProfile | null>} El perfil del usuario o null si falla
   */
  const fetchProfile = async (): Promise<UserProfile | null> => {
    setLoading(true);
    setError(null);

    try {
      const result = await AuthService.getCurrentProfile();
      setProfile(result);
      return result;
    } catch (err: any) {
      const errorMessage = err.message || "Error al cargar el perfil";
      setError(errorMessage);
      return null;
    } finally {
      setLoading(false);
    }
  };

  return {
    profile,
    loading,
    error,
    fetchProfile,
  };
}
