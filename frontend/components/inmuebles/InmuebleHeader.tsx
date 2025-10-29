"use client"

import InmuebleIcon from "@/components/inmueble-icon"
import { Button } from "@/components/ui/button"
import { ArrowLeft } from "lucide-react"

type Props = {
  direccion: string
  tipoInmuebleId: number
  tipoNombre: string
}

export default function InmuebleHeader({ direccion, tipoInmuebleId, tipoNombre }: Props) {
  return (
    <div className="mb-8 flex flex-col gap-3">
      <Button variant="outline" onClick={() => window.history.back()} className="w-fit">
        <ArrowLeft className="h-4 w-4 mr-2" />
        Volver
      </Button>
      <div className="flex items-center m-5">
        <InmuebleIcon tipoInmuebleId={tipoInmuebleId} className="h-15 w-15 mr-2" />
        <div>
          <h2 className="text-3xl font-bold text-foreground font-sans">{direccion}</h2>
          <p className="text-muted-foreground">{tipoNombre || "Inmueble"}</p>
        </div>
      </div>
    </div>
  )
}
