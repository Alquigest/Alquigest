"use client"

import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { Check, CheckCircle2 } from "lucide-react"

interface ModalAumentoExitosoProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  nuevoMonto: number
  direccionInmueble: string
}

export default function ModalAumentoExitoso({
  open,
  onOpenChange,
  nuevoMonto,
  direccionInmueble
}: ModalAumentoExitosoProps) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[450px]">
        <DialogHeader>
          <div className="flex flex-col items-center gap-4 py-4">
            <div className="rounded-full bg-green-100 p-3">
              <CheckCircle2 className="h-12 w-12 text-green-600" />
            </div>
            <DialogTitle className="text-xl text-center">
              ¡Aumento Aplicado con Éxito!
            </DialogTitle>
          </div>
        </DialogHeader>

        <DialogDescription className="text-center space-y-3 ">
          <div>
            <p className="text-base text-muted-foreground">
              Inmueble:
            </p>
            <p className="text-lg font-semibold text-foreground">
              {direccionInmueble}
            </p>
          </div>

          <div className="rounded-lg bg-card border border-card p-4">
            <p className="text-base text-muted-foreground mb-3">
              Monto de alquiler actualizado:
            </p>
            <p className="text-3xl font-bold text-green-600">
              ${nuevoMonto.toLocaleString("es-AR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </p>
          </div>

        </DialogDescription>

        <DialogFooter className="flex justify-center mt-4">
          <Button
            onClick={() => onOpenChange(false)}
            className="bg-green-600 hover:bg-green-700 w-full"
          >
            <Check />
            Aceptar
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
