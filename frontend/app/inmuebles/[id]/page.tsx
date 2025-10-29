"use client"
import { useParams } from "next/navigation"
import DetalleInmuebleContainer from "@/components/inmuebles/DetalleInmuebleContainer"

export default function DetalleInmueble() {
  const params = useParams()
  const id = params.id as string

  return (
    <div className="min-h-screen bg-background">
      <DetalleInmuebleContainer id={id} />
    </div>
  )
}