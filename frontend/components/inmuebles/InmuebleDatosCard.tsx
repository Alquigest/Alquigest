"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { BuildingIcon } from "lucide-react"

type Props = {
  direccion?: string
  tipoNombre?: string
  estadoNombre?: string
  superficie?: number
}

export default function InmuebleDatosCard({ direccion, tipoNombre, estadoNombre, superficie }: Props) {
  return (
    <Card className="max-w-4xl mx-auto mb-10">
      <CardHeader>
        <div className="flex items-center gap-2">
          <BuildingIcon className="h-5 w-5" />
          <CardTitle className="font-bold">Datos del Inmueble</CardTitle>
        </div>
      </CardHeader>

      <CardContent>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 justify-between">
          <div className="flex gap-3">
            <h2 className="font-bold">Dirección:</h2>
            <p className="text-muted-foreground">{direccion}</p>
          </div>
          <div className="flex gap-3">
            <h2 className="font-bold">Tipo de Inmueble:</h2>
            <p className="text-muted-foreground">{tipoNombre || "No disponible"}</p>
          </div>
          <div className="flex gap-3">
            <h2 className="font-bold">Estado:</h2>
            <p className="text-muted-foreground">{estadoNombre || "No disponible"}</p>
          </div>
          <div className="flex gap-3">
            <h2 className="font-bold">Superficie:</h2>
            <p className="text-muted-foreground">{superficie ? `${superficie} m²` : "No especificada"}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
