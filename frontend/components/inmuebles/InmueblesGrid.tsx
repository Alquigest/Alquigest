"use client"

import InmuebleCard from "@/components/inmuebles/InmuebleCard"
import { Inmueble } from "@/types/Inmueble"
import { Propietario } from "@/types/Propietario"

type Props = {
  inmuebles: Inmueble[]
  propietarios: Propietario[]
  canEdit: boolean
  onEdit: (inmueble: Inmueble) => void
}

export default function InmueblesGrid({ inmuebles, propietarios, canEdit, onEdit }: Props) {
  const propietarioNombre = (propietarioId: number) => {
    const p = propietarios.find((x) => x.id === propietarioId)
    return p ? `${p.apellido}, ${p.nombre}` : undefined
  }
  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
      {inmuebles.map((inmueble) => (
        <InmuebleCard
          key={inmueble.id}
          inmueble={inmueble}
          propietarioNombre={propietarioNombre(inmueble.propietarioId)}
          canEdit={canEdit}
          onEdit={onEdit}
        />)
      )}
    </div>
  )
}
