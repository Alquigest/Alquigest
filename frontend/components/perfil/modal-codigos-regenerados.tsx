"use client";

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { CodigosRegeneradosResponse } from "@/utils/services/codigosSeguridadService";
import { KeySquare, AlertTriangle, Copy, Check } from "lucide-react";
import { useState } from "react";
import { Button } from "@/components/ui/button";

interface ModalCodigosRegeneradosProps {
  isOpen: boolean;
  onClose: () => void;
  codigosData: CodigosRegeneradosResponse | null;
}

export default function ModalCodigosRegenerados({
  isOpen,
  onClose,
  codigosData,
}: ModalCodigosRegeneradosProps) {
  const [copiedIndex, setCopiedIndex] = useState<number | null>(null);

  if (!codigosData) return null;

  const handleCopyCodigo = async (codigo: string, index: number) => {
    try {
      await navigator.clipboard.writeText(codigo);
      setCopiedIndex(index);
      setTimeout(() => setCopiedIndex(null), 2000);
    } catch (err) {
      console.error("Error al copiar:", err);
    }
  };

  const handleCopyAll = async () => {
    try {
      const allCodigos = codigosData.codigos.join("\n");
      await navigator.clipboard.writeText(allCodigos);
      setCopiedIndex(-1);
      setTimeout(() => setCopiedIndex(null), 2000);
    } catch (err) {
      console.error("Error al copiar:", err);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[85vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-xl">
            <KeySquare className="h-6 w-6 text-primary" />
            Códigos de Seguridad Regenerados
          </DialogTitle>
          <DialogDescription>
            Guarde estos códigos en un lugar seguro
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6 pt-4">
          {/* Mensaje de Advertencia */}
          <div className="flex items-start gap-3 p-4 rounded-lg bg-red-200 dark:bg-yellow-950/20 border border-destructive dark:border-red-900">
            <AlertTriangle className="h-5 w-5 text-red-950 dark:text-red-400 shrink-0 mt-0.5" />
            <p className="text-sm text-red-950 dark:text-red-200">
              {codigosData.mensaje}
            </p>
          </div>

          {/* Lista de Códigos */}
          <div>
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-sm font-semibold text-foreground">
                Sus nuevos códigos ({codigosData.codigos.length})
              </h3>
              <Button
                variant="outline"
                size="sm"
                onClick={handleCopyAll}
                className="text-xs"
              >
                {copiedIndex === -1 ? (
                  <>
                    <Check className="h-3 w-3 mr-1" />
                    Copiados
                  </>
                ) : (
                  <>
                    <Copy className="h-3 w-3 mr-1" />
                    Copiar Todos
                  </>
                )}
              </Button>
            </div>

            <div className="space-y-2">
              {codigosData.codigos.map((codigo, index) => (
                <div
                  key={index}
                  className="flex items-center justify-between p-3 rounded-lg bg-primary/5 border border-primary/20 hover:bg-primary/10 transition-colors group"
                >
                  <div className="flex items-center gap-3">
                    <span className="text-xs font-medium text-muted-foreground w-6">
                      {index + 1}.
                    </span>
                    <code className="text-sm font-mono font-semibold text-foreground">
                      {codigo}
                    </code>
                  </div>
                  <Button
                    variant="ghost"
                    size="sm"
                    onClick={() => handleCopyCodigo(codigo, index)}
                    className="opacity-0 group-hover:opacity-100 transition-opacity"
                  >
                    {copiedIndex === index ? (
                      <Check className="h-4 w-4 text-green-600" />
                    ) : (
                      <Copy className="h-4 w-4" />
                    )}
                  </Button>
                </div>
              ))}
            </div>
          </div>

          {/* Información adicional */}
          <div className="p-4 rounded-lg bg-muted/50 border border-border">
            <p className="text-xs text-muted-foreground text-center">
              Estos códigos solo se muestran una vez. Asegúrese de guardarlos en un lugar seguro
              antes de cerrar esta ventana.
            </p>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
