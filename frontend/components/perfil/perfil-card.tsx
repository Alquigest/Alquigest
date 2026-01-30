"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { UserProfile } from "@/utils/services/authService";
import { Lock, Mail, Shield, User, KeySquare, Plus } from "lucide-react";
import { useState } from "react";
import LoadingDefault from "@/components/loading-default";
import ModalPermisos from "./modal-permisos";
import ModalCodigosRegenerados from "./modal-codigos-regenerados";
import { useCodigosSeguridad } from "@/hooks/useCodigosSeguridad";
import ModalError from "@/components/modal-error";
import ModalDefault from "../modal-default";
import ModalConfirmacion from "../modal-confirmacion";

interface PerfilCardProps {
  profile: UserProfile | null;
  loading: boolean;
  error: string | null;
}

export default function PerfilCard({
  profile,
  loading,
  error,
}: PerfilCardProps) {
  const [showPermissionsModal, setShowPermissionsModal] = useState(false);
  const [showCodigosRegeneradosModal, setShowCodigosRegeneradosModal] = useState(false);
  const [showConfirmacionModal, setShowConfirmacionModal] = useState(false);
  const [showDisponiblesInfo, setShowDisponiblesInfo] = useState(false);
  
  const {
    checkDisponibles,
    regenerarCodigos,
    loading: loadingCodigos,
    error: errorCodigos,
    codigosInfo,
    codigosRegenerados,
  } = useCodigosSeguridad();

  const handleCheckCodigos = async () => {
    const result = await checkDisponibles();
    if (result) {
      setShowDisponiblesInfo(true);
    }
  };

  const handleGenerarCodigos = () => {
    setShowConfirmacionModal(true);
  };

  const handleConfirmGenerar = async () => {
    setShowConfirmacionModal(false);
    const result = await regenerarCodigos();
    if (result) {
      setShowCodigosRegeneradosModal(true);
    }
  };

  if (loading) {
    return <LoadingDefault />;
  }

  if (error) {
    return (
      <Card className="w-full max-w-2xl mx-auto border-destructive">
        <CardHeader>
          <CardTitle className="text-destructive">Error</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-muted-foreground">{error}</p>
        </CardContent>
      </Card>
    );
  }

  if (!profile) {
    return null;
  }

  return (
    <>
      <div className="w-full  mx-auto space-y-4">
        <div className="">
            {/* Card de Información Básica */}
            <Card className="border-primary/20">
            <CardHeader>
                <CardTitle className="flex items-center gap-2">
                <Shield className="h-6 w-6 text-primary" />
                Datos Principales
                </CardTitle>
            </CardHeader>
            <CardContent className="pt-6">
                <div className="space-y-4">
                {/* Usuario */}
                <div className="flex items-start gap-3 pb-4 border-b border-border/40">
                    <User className="h-6 w-6 text-primary mt-1 shrink-0" />
                    <div className="flex-1">
                    <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wide">
                        Usuario
                    </p>
                    <p className="text-base font-semibold text-foreground">
                        {profile.username}
                    </p>
                    </div>
                </div>

                {/* Email */}
                <div className="flex items-start gap-3 pb-4 border-b border-border/40">
                    <Mail className="h-6 w-6 text-primary mt-1 shrink-0" />
                    <div className="flex-1">
                    <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wide">
                        Correo Electrónico
                    </p>
                    <p className="text-base font-semibold text-foreground">
                        {profile.email}
                    </p>
                    </div>
                </div>

                {/* Roles */}
                <div className="flex items-start gap-3">
                    <Lock className="h-6 w-6 text-primary mt-1 shrink-0" />
                    <div className="flex-1">
                    <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wide">
                        Rol
                    </p>
                    <div className="flex flex-wrap gap-2 mt-2">
                        {profile.roles.map((role) => (
                        <span
                            key={role}
                            className="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-primary/10 text-primary border border-primary/20"
                        >
                            {role.replace("ROLE_", "")}
                        </span>
                        ))}
                    </div>
                    </div>
                </div>
                </div>
            </CardContent>
            </Card>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5">   
            {/* Card de Permisos */}
            <Card className="border-primary/20">
                <CardHeader>
                    <CardTitle className="flex items-center gap-2">
                    <Shield className="h-6 w-6 text-primary" />
                    Permisos del Perfil
                    </CardTitle>
            </CardHeader>
            <CardContent className="pt-6">
                <Button
                onClick={() => setShowPermissionsModal(true)}
                className="w-full"
                variant="outline"
                >
                <Shield className="h-4 w-4 mr-2" />
                Ver Permisos del Perfil
                </Button>
            </CardContent>
            </Card>

            {/* Card de Códigos de Seguridad */}
            <Card className="border-primary/20">
            <CardHeader>
                <CardTitle className="flex items-center gap-2">
                <KeySquare className="h-6 w-6 text-primary" />
                Códigos de Seguridad
                </CardTitle>
            </CardHeader>
            <CardContent className="pt-6">
                <div className="flex flex-col sm:flex-row gap-3">
                <Button
                    onClick={handleCheckCodigos}
                    className="flex-1"
                    variant="outline"
                    loading={loadingCodigos}
                >
                    <KeySquare className="h-4 w-4 mr-2" />
                    Verificar Códigos Disponibles
                </Button>
                <Button
                    onClick={handleGenerarCodigos}
                    className="flex-1"
                    variant="outline"
                    loading={loadingCodigos}
                >
                    <Plus className="h-4 w-4 mr-2" />
                    Generar Nuevos Códigos
                </Button>
                </div>
            </CardContent>
            </Card>
        </div>
      </div>

      {/* Modal de Permisos */}
      <ModalPermisos
        isOpen={showPermissionsModal}
        onClose={() => setShowPermissionsModal(false)}
        permisos={profile.permisos}
      />

      {/* Modal de Códigos Disponibles */}
      {showDisponiblesInfo && codigosInfo && (
        <ModalDefault
          onClose={() => setShowDisponiblesInfo(false)}
          titulo="Códigos Únicos de Verificación"
          mensaje={codigosInfo.mensaje}
        />
      )}

      {/* Modal de Confirmación para Regenerar */}
      <ModalConfirmacion
        open={showConfirmacionModal}
        titulo="¿Regenerar códigos de seguridad?"
        mensaje="Esto invalidará todos sus códigos actuales y generará nuevos. Esta acción no se puede deshacer. ¿Desea continuar?"
        onConfirm={handleConfirmGenerar}
        onCancel={() => setShowConfirmacionModal(false)}
      />

      {/* Modal de Códigos Regenerados */}
      <ModalCodigosRegenerados
        isOpen={showCodigosRegeneradosModal}
        onClose={() => setShowCodigosRegeneradosModal(false)}
        codigosData={codigosRegenerados}
      />

      {/* Modal de Error para Códigos */}
      {errorCodigos && (
        <ModalError
          titulo="Error"
          mensaje={errorCodigos}
          onClose={() => {}}
        />
      )}
    </>
  );
}
