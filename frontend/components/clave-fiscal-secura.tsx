'use client'

import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { Eye, Copy, EyeOff } from 'lucide-react';
import { fetchWithToken } from '@/utils/functions/auth-functions/fetchWithToken';
import BACKEND_URL from '@/utils/backendURL';

interface ClaveFiscalSecuraProps {
  propietarioId: string;
  claveFiscalEnmascarada: string | null;
}

export default function ClaveFiscalSecura({
  propietarioId,
  claveFiscalEnmascarada
}: ClaveFiscalSecuraProps) {
  const [claveFiscalVisible, setClaveFiscalVisible] = useState(false);
  const [claveFiscal, setClaveFiscal] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [timeLeft, setTimeLeft] = useState(30);

  // Timer para auto-ocultar después de 30 segundos
  useEffect(() => {
    let interval: NodeJS.Timeout | null = null;

    if (claveFiscalVisible && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft((prev) => {
          if (prev <= 1) {
            setClaveFiscalVisible(false);
            setClaveFiscal(null);
            return 30;
          }
          return prev - 1;
        });
      }, 1000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [claveFiscalVisible, timeLeft]);

  const revelarClaveFiscal = async () => {
    // Solicitar confirmación con contraseña
    const password = prompt("⚠️ Por seguridad, ingresa tu contraseña para revelar la clave fiscal:");

    if (!password) return;

    setLoading(true);

    try {
      const response = await fetchWithToken(
        `${BACKEND_URL}/propietarios/${propietarioId}/clave-fiscal/revelar`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ password })
        }
      );

      setClaveFiscal(response.claveFiscal);
      setClaveFiscalVisible(true);
      setTimeLeft(30);

    } catch (error: any) {
      if (error.message?.includes('401') || error.message?.includes('Unauthorized')) {
        alert("❌ Contraseña incorrecta");
      } else {
        alert("❌ Error al revelar la clave fiscal: " + (error.message || 'Error desconocido'));
      }
      console.error("Error revelando clave fiscal:", error);
    } finally {
      setLoading(false);
    }
  };

  const copiarClave = () => {
    if (claveFiscal) {
      navigator.clipboard.writeText(claveFiscal);
      alert("✅ Clave fiscal copiada al portapapeles");
    }
  };

  const ocultarClave = () => {
    setClaveFiscalVisible(false);
    setClaveFiscal(null);
    setTimeLeft(30);
  };

  return (
    <div className="flex flex-col gap-2">
      {!claveFiscalVisible ? (
        <div className="flex items-center gap-3">
          <code className="px-3 py-2 bg-muted rounded-md font-mono text-sm border">
            {claveFiscalEnmascarada || '****'}
          </code>
          <Button
            onClick={revelarClaveFiscal}
            disabled={loading}
            variant="outline"
            size="sm"
            className="border-yellow-600 text-yellow-700 hover:bg-yellow-50"
          >
            {loading ? (
              <>⏳ Procesando...</>
            ) : (
              <>
                <Eye className="h-4 w-4 mr-2" />
                Revelar
              </>
            )}
          </Button>
        </div>
      ) : (
        <Card className="border-red-500 border-2 bg-yellow-50 p-4">
          <div className="flex flex-col gap-3">
            <div className="flex items-center justify-between">
              <strong className="text-red-600 text-sm">
                ⚠️ CLAVE FISCAL CONFIDENCIAL
              </strong>
              <span className="text-sm text-muted-foreground">
                Se ocultará en {timeLeft}s
              </span>
            </div>

            <code className="px-4 py-3 bg-white rounded-md font-mono text-lg font-bold border-2 border-red-500 select-all cursor-text">
              {claveFiscal}
            </code>

            <div className="flex gap-2">
              <Button
                onClick={copiarClave}
                size="sm"
                variant="default"
                className="bg-green-600 hover:bg-green-700"
              >
                <Copy className="h-4 w-4 mr-2" />
                Copiar
              </Button>
              <Button
                onClick={ocultarClave}
                size="sm"
                variant="destructive"
              >
                <EyeOff className="h-4 w-4 mr-2" />
                Ocultar
              </Button>
            </div>

            <p className="text-xs text-yellow-800 italic">
              ⚠️ Esta clave es confidencial. No la compartas con terceros.
            </p>
          </div>
        </Card>
      )}
    </div>
  );
}

