"use client"

import { useState } from "react"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { TrendingUp, Loader2 } from "lucide-react"
import { fetchJSON } from "@/utils/functions/fetchWithCredentials"

interface ModalAumentoManualProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  alquilerId: number
  direccionInmueble: string
  onSuccess: (nuevoMonto: number) => void
}

interface AumentoManualRequest {
  indiceInicial: number
  indiceFinal: number
}

interface AumentoManualResponse {
  id: number
  contratoId: number
  fechaVencimientoPago: string
  monto: number
  estaPagado: boolean
  fechaPago: string | null
  necesitaAumentoManual: boolean
  cuentaBanco: string | null
  titularDePago: string | null
  metodo: string | null
  createdAt: string
  updatedAt: string
  inmuebleId: number
  direccionInmueble: string
  inquilinoId: number
  nombreInquilino: string
  apellidoInquilino: string
}

export default function ModalAumentoManual({
  open,
  onOpenChange,
  alquilerId,
  direccionInmueble,
  onSuccess
}: ModalAumentoManualProps) {
  const [indiceInicial, setIndiceInicial] = useState("")
  const [indiceFinal, setIndiceFinal] = useState("")
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    // Validaciones
    if (!indiceInicial || !indiceFinal) {
      setError("Ambos índices son requeridos")
      return
    }

    const indiceInicialNum = parseFloat(indiceInicial)
    const indiceFinalNum = parseFloat(indiceFinal)

    if (isNaN(indiceInicialNum) || isNaN(indiceFinalNum)) {
      setError("Los índices deben ser números válidos")
      return
    }

    if (indiceInicialNum <= 0 || indiceFinalNum <= 0) {
      setError("Los índices deben ser mayores a 0")
      return
    }

    setLoading(true)

    try {
      const requestBody: AumentoManualRequest = {
        indiceInicial: indiceInicialNum,
        indiceFinal: indiceFinalNum
      }

      const response = await fetchJSON<AumentoManualResponse>(
        `/alquileres/${alquilerId}/aumento-manual`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(requestBody)
        }
      )

      // Éxito - llamar al callback con el nuevo monto
      onSuccess(response.monto)
      
      // Resetear formulario
      setIndiceInicial("")
      setIndiceFinal("")
      
      // Cerrar modal
      onOpenChange(false)
    } catch (err: any) {
      console.error("Error al aplicar aumento manual:", err)
      setError(err.message || "Error al aplicar el aumento manual")
    } finally {
      setLoading(false)
    }
  }

  const handleClose = () => {
    if (!loading) {
      setIndiceInicial("")
      setIndiceFinal("")
      setError(null)
      onOpenChange(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-xl">
            <TrendingUp className="h-6 w-6 text-orange-500" />
            Aplicar Aumento Manual
          </DialogTitle>
          <DialogDescription className="text-base pt-2">
            <span className="font-semibold">{direccionInmueble}</span>
            <br />
            Ingrese los índices ICL para calcular el nuevo monto del alquiler
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit}>
          <div className="grid gap-6 py-4">
            {/* Índice Inicial */}
            <div className="grid gap-2">
              <Label htmlFor="indiceInicial" className="text-base">
                Índice ICL Inicial
              </Label>
              <Input
                id="indiceInicial"
                type="number"
                step="0.01"
                placeholder="Ej: 100.00"
                value={indiceInicial}
                onChange={(e) => setIndiceInicial(e.target.value)}
                disabled={loading}
                className="text-base"
              />
            </div>

            {/* Índice Final */}
            <div className="grid gap-2">
              <Label htmlFor="indiceFinal" className="text-base">
                Índice ICL Final
              </Label>
              <Input
                id="indiceFinal"
                type="number"
                step="0.01"
                placeholder="Ej: 115.50"
                value={indiceFinal}
                onChange={(e) => setIndiceFinal(e.target.value)}
                disabled={loading}
                className="text-base"
              />
            </div>

            {/* Mensaje de error */}
            {error && (
              <div className="rounded-md bg-red-50 border border-red-200 p-3">
                <p className="text-sm text-red-800">{error}</p>
              </div>
            )}
          </div>

          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={handleClose}
              disabled={loading}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              
              disabled={loading}
            >
              {loading ? (
                <>
                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  Aplicando...
                </>
              ) : (
                <>
                  <TrendingUp className="mr-2 h-4 w-4" />
                  Aplicar Aumento
                </>
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}
