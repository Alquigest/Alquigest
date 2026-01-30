"use client";

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { UserPermissions } from "@/utils/services/authService";
import { Shield } from "lucide-react";

interface ModalPermisosProps {
  isOpen: boolean;
  onClose: () => void;
  permisos: UserPermissions;
}

/**
 * Convierte un nombre de permiso en formato legible
 * @example permiso_camel_case -> Permiso Camel Case
 */
function formatPermissionName(permission: string): string {
  return permission
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
}

/**
 * Agrupa permisos por estado (true/false)
 */
function groupPermissionsByStatus(
  permisos: UserPermissions
): { enabled: [string, boolean][]; disabled: [string, boolean][] } {
  const entries = Object.entries(permisos);
  return {
    enabled: entries.filter(([, value]) => value),
    disabled: entries.filter(([, value]) => !value),
  };
}

export default function ModalPermisos({
  isOpen,
  onClose,
  permisos,
}: ModalPermisosProps) {
  const { enabled, disabled } = groupPermissionsByStatus(permisos);

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-3xl max-h-[80vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-xl">
            <Shield className="h-6 w-6 text-primary" />
            Permisos del Perfil
          </DialogTitle>
          <DialogDescription>
            Visualiza todos los permisos asignados a tu perfil
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6 pt-4">
          {/* Permisos Habilitados */}
          <div>
            <h3 className="text-sm font-semibold text-green-600 dark:text-green-400 mb-3 flex items-center gap-2">
              <span className="h-2 w-2 rounded-full bg-green-600 dark:bg-green-400"></span>
              Permisos Habilitados ({enabled.length})
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
              {enabled.length > 0 ? (
                enabled.map(([permission]) => (
                  <div
                    key={permission}
                    className="flex items-center gap-2 p-2 rounded bg-card border border-border"
                  >
                    <span className="h-2 w-2 rounded-full bg-green-600 dark:bg-green-400 shrink-0"></span>
                    <span className="text-sm text-green-900 dark:text-green-200">
                      {formatPermissionName(permission)}
                    </span>
                  </div>
                ))
              ) : (
                <p className="text-xs text-muted-foreground col-span-2">
                  No hay permisos habilitados
                </p>
              )}
            </div>
          </div>

          {/* Permisos Deshabilitados */}
          {disabled.length > 0 && (
            <div>
              <h3 className="text-sm font-semibold text-red-600 dark:text-red-400 mb-3 flex items-center gap-2">
                <span className="h-2 w-2 rounded-full bg-red-600 dark:bg-red-400"></span>
                Permisos Deshabilitados ({disabled.length})
              </h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
                {disabled.map(([permission]) => (
                  <div
                    key={permission}
                    className="flex items-center gap-2 p-2 rounded bg-card border border-border"
                  >
                    <span className="h-2 w-2 rounded-full bg-red-600 dark:bg-red-400 shrink-0"></span>
                    <span className="text-sm text-red-900 dark:text-red-200 line-through opacity-75">
                      {formatPermissionName(permission)}
                    </span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}
