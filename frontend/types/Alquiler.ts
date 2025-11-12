interface AlquilerItem {
  id: number
  contratoId: number
  fechaVencimientoPago: string
  monto: number
  estaPagado: boolean
  fechaPago: string | null
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