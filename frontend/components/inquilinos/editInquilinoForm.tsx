import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Inquilino } from "@/types/Inquilino";
import { useRef, useEffect } from "react";
import BusquedaDesplegable, { BusquedaDesplegableRef } from "@/components/busqueda/busqueda-desplegable"
import { barrios } from "@/utils/barrios"

export default function editInquilinoForm({editingInquilino, setEditingInquilino, setIsEditInquilinoOpen, handleUpdateInquilino, loadingActualizacion}: {
    editingInquilino: Partial<Inquilino> | null,
    setEditingInquilino: (inquilino: any) => void,
    setIsEditInquilinoOpen: (isOpen: boolean) => void,
    handleUpdateInquilino: () => void,
    loadingActualizacion: boolean
}) {
    const barrioRef = useRef<BusquedaDesplegableRef>(null)

    useEffect(() => {
      // Setear el valor inicial del barrio cuando cambia editingInquilino
      if (editingInquilino?.barrio && barrioRef.current) {
        barrioRef.current.setDisplayValue(editingInquilino.barrio)
      }
    }, [editingInquilino])
    return(
        <div>{editingInquilino && (
              <form
                className="space-y-4"
                onSubmit={(e) => {
                  e.preventDefault()
                  handleUpdateInquilino()
                }}
              >
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <Label htmlFor="edit-nombre">Nombre</Label>
                    <Input
                      required
                      id="edit-nombre"
                      value={editingInquilino.nombre}
                      onChange={(e) => setEditingInquilino({ ...editingInquilino, nombre: e.target.value })}
                    />
                  </div>
              
                  <div>
                    <Label htmlFor="edit-apellido">Apellido</Label>
                    <Input
                      required
                      id="edit-apellido"
                      value={editingInquilino.apellido}
                      onChange={(e) => setEditingInquilino({ ...editingInquilino, apellido: e.target.value })}
                    />
                  </div>
                  
                </div>

                <div>
                  <Label htmlFor="edit-cuil">CUIL</Label>
                  <Input id="edit-cuil" value={editingInquilino.cuil} disabled className="bg-muted" />
                  <p className="text-xs text-muted-foreground mt-1">El CUIL no se puede modificar</p>
                </div>

                <div>
                  <Label htmlFor="telefono">Teléfono</Label>
                  <Input
                    id="telefono"
                    type="tel"
                    maxLength={12}
                    value={editingInquilino.telefono}
                    onChange={(e) =>{
                      const value = e.target.value.replace(/[^0-9()+-\s]/g, "");
                      setEditingInquilino({ ...editingInquilino, telefono: value })
                    }}
                    placeholder="351-4455667"
                  />
                </div>
                <div>
                    <Label htmlFor="edit-direccion">Dirección</Label>
                    <Input
                      required
                      id="edit-direccion"
                      value={editingInquilino.direccion}
                      onChange={(e) => setEditingInquilino({ ...editingInquilino, direccion: e.target.value })}
                    />
                  </div>
                  <div>
                    <Label htmlFor="edit-barrio">Barrio</Label>
                    <BusquedaDesplegable
                      ref={barrioRef}
                      items={barrios}
                      propiedadesBusqueda={["Nombre"]}
                      onSelect={(barrio) =>
                        setEditingInquilino({ ...editingInquilino, barrio: barrio.Nombre })
                      }
                      placeholder="Buscar barrio..."
                      getItemLabel={(barrio) => barrio.Nombre}
                    />
                  </div>

                <div>
                  <Label htmlFor="edit-estado">Estado</Label>
                  <Select
                    value={editingInquilino.esActivo ? "true" : "false"}
                    onValueChange={(value) => setEditingInquilino({ ...editingInquilino, esActivo: value === "true" })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="true">Activo</SelectItem>
                      <SelectItem value="false">Inactivo</SelectItem>
                    </SelectContent>
                  </Select>
                </div>

                <div className="flex gap-2 pt-4">
                  <Button 
                    type="submit" 
                    className="flex-1"
                    loading={loadingActualizacion}
                  >
                    Guardar Cambios
                  </Button>
                  <Button
                    type="button"
                    variant="outline"
                    onClick={() => setIsEditInquilinoOpen(false)}
                    className="flex-1"
                    disabled={loadingActualizacion}
                  >
                    Cancelar
                  </Button>
                </div>
              </form>
            )}</div>
    )
}