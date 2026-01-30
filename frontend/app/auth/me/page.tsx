"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/contexts/AuthProvider";
import PerfilCard from "@/components/perfil/perfil-card";
import { useUserProfile } from "@/hooks/useUserProfile";
import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";

export default function PerfilPage() {
  const { isAuthenticated, isLoading: authLoading } = useAuth();
  const { profile, loading, error, fetchProfile } = useUserProfile();
  const router = useRouter();

  // Obtener el perfil cuando el componente se monta
  useEffect(() => {
    if (isAuthenticated && !authLoading) {
      fetchProfile();
    }
  }, [isAuthenticated, authLoading]);

  // Redirigir a login si no está autenticado
  useEffect(() => {
    if (!authLoading && !isAuthenticated) {
      router.push("/auth/login");
    }
  }, [isAuthenticated, authLoading, router]);

  if (authLoading || loading) {
    return null; // El loading se muestra en el componente PerfilCard
  }

  return (
    <main className="container h-screen mx-auto px-6 py-8 pt-25 sm:pt-28">
      <div className="flex flex-col gap-3 mb-8">
        <Button
          variant="outline"
          onClick={() => router.back()}
          className="w-fit"
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Volver
        </Button>
        <div className="flex items-center gap-2 m-5">
          <h1 className="text-3xl font-bold">Mi Perfil</h1>
        </div>
      </div>

      <PerfilCard profile={profile} loading={loading} error={error} />
    </main>
  );
}
