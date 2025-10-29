"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { User } from "lucide-react"
import Link from "next/link"
import { Button } from "@/components/ui/button"

export type PropietarioView = {
  id: number
  apellido?: string
  nombre?: string
  cuil?: string
  telefono?: string
  email?: string
  direccion?: string
}

type Props = {
  propietario?: PropietarioView
}

export default function PropietarioCard({ propietario }: Props) {
  if (!propietario) return null
  return (
    <Card className="max-w-4xl mx-auto">
      <CardHeader>
        <div className="flex items-center gap-2">
          <User className="h-5 w-5" />
          <CardTitle className="font-bold flex gap-2">
            <Link href={`/propietarios/${propietario.id}`} className="flex gap-2 hover:text-primary">
              <p>Propietario:</p>
              <p>
                {propietario.apellido}, {propietario.nombre}
              </p>
            </Link>
          </CardTitle>
        </div>
      </CardHeader>

      <CardContent>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 justify-between">
          <div className="flex gap-3">
            <h2 className="font-bold">CUIL:</h2>
            <p className="text-muted-foreground">{propietario.cuil}</p>
          </div>
          <div className="flex gap-3">
            <h2 className="font-bold">Telefono:</h2>
            <p className="text-muted-foreground">{propietario.telefono}</p>
          </div>
          <div className="flex gap-3">
            <h2 className="font-bold">Email:</h2>
            <p className="text-muted-foreground">{propietario.email}</p>
          </div>
          <div className="flex gap-3">
            <h2 className="font-bold">Dirección:</h2>
            <p className="text-muted-foreground">{propietario.direccion}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
