"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Handshake } from "lucide-react"
import Link from "next/link"

export type ContratoResumen = {
  id: number
  apellidoInquilino: string
  nombreInquilino: string
  fechaInicio: string
  fechaFin: string
}

type Props = {
  contratos: ContratoResumen[]
}

export default function ContratoResumenCard({ contratos }: Props) {
  return (
    <Card className="max-w-4xl mx-auto mt-10">
      <CardHeader>
        <div className="flex items-center gap-2">
          <Handshake className="h-5 w-5" />
          <CardTitle className="font-bold flex gap-2 ">
            <p>Contrato de Alquiler</p>
          </CardTitle>
        </div>
      </CardHeader>

      <CardContent>
        <div className="grid grid-cols-1 md:grid-cols-2  justify-between">
          {contratos.length === 0 && (
            <div>
              <p>El inmueble no se encuentra en un contrato de alquiler vigente</p>
            </div>
          )}

          {contratos.length !== 0 && (
            <div className="flex flex-col w-full gap-3">
              <p>El inmueble se encuentra en un contrato vigente</p>
              <div className="flex gap-3">
                <h2 className="font-bold">Locatario:</h2>
                <p className="text-muted-foreground">
                  {contratos[0].apellidoInquilino}, {contratos[0].nombreInquilino}
                </p>
              </div>
              <div className="flex gap-3">
                <h2 className="font-bold">Fecha de Inicio:</h2>
                <p className="text-muted-foreground">{contratos[0].fechaInicio}</p>
              </div>
              <div className="flex gap-3">
                <h2 className="font-bold">Fecha Finalización:</h2>
                <p className="text-muted-foreground">{contratos[0].fechaFin}</p>
              </div>
              <Link href={`/contratos/${contratos[0].id}`} className="mt-2">
                <Button variant={"outline"}>Ver detalles</Button>
              </Link>
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  )
}
