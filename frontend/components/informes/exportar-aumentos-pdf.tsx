"use client"

import { Button } from "@/components/ui/button"
import { FileDown } from "lucide-react"
import jsPDF from "jspdf"

export interface AumentoItemPDF {
  aumentoId: number
  fechaAumento: string
  montoAnterior: number
  montoNuevo: number
  porcentajeAumento: number
}

export interface AumentoPorContratoPDF {
  contratoId: number
  direccionInmueble: string
  nombreInquilino: string
  apellidoInquilino: string
  nombrePropietario: string
  apellidoPropietario: string
  aumentos: AumentoItemPDF[]
}

interface ExportarAumentosPDFProps {
  periodoDesde: string
  periodoHasta: string
  contratos: AumentoPorContratoPDF[]
  totalAumentos?: number
  disabled?: boolean
  onBeforeGenerate?: () => Promise<void> | void
}

const formatoMoneda = (valor: number) =>
  valor.toLocaleString("es-AR", { style: "currency", currency: "ARS", minimumFractionDigits: 2 })

// Evita desfase de mes (YYYY-MM-DD -> local)
const formatoMesAnioLocal = (fecha: string) => {
  if (!fecha) return "--/----"
  const m = fecha.match(/^(\d{4})-(\d{2})-(\d{2})$/)
  if (m) {
    const [, y, mm, dd] = m
    const d = new Date(Number(y), Number(mm) - 1, Number(dd))
    return d.toLocaleDateString("es-AR", { year: "numeric", month: "long" })
  }
  const d = new Date(fecha)
  return d.toLocaleDateString("es-AR", { year: "numeric", month: "long", timeZone: "UTC" as const })
}

export default function ExportarAumentosPDF({
  periodoDesde,
  periodoHasta,
  contratos,
  totalAumentos,
  disabled,
  onBeforeGenerate,
}: ExportarAumentosPDFProps) {
  const cargarImagen = (src: string): Promise<string> => {
    return new Promise((resolve, reject) => {
      const img = new Image()
      img.crossOrigin = "anonymous"
      img.onload = () => {
        const canvas = document.createElement("canvas")
        const ctx = canvas.getContext("2d")
        canvas.width = img.width
        canvas.height = img.height
        ctx?.drawImage(img, 0, 0)
        resolve(canvas.toDataURL("image/png"))
      }
      img.onerror = reject
      img.src = src
    })
  }

  const generarPDF = async () => {
    const doc = new jsPDF({ orientation: "portrait", unit: "pt", format: "a4" })
    const fechaActual = new Date()
    const fechaCompleta = fechaActual.toLocaleDateString("es-AR")

    // Logo
    try {
      const logo = await cargarImagen("/alquigest-dark.png")
      doc.addImage(logo, "PNG", 30, 30, 110, 20)
    } catch {}

    // Encabezado
    doc.setFont("helvetica", "bold")
    doc.setFontSize(18)
    doc.setTextColor(0, 50, 100)
    doc.text("Historial de Aumentos de Alquileres", 30, 80)

    doc.setFont("helvetica", "bold")
    doc.setFontSize(11)
    doc.setTextColor(0, 0, 0)
    doc.text(`Período desde: ${periodoDesde}`, 30, 100)
    doc.text(`Período hasta: ${periodoHasta}`, 30, 115)
    doc.setFont("helvetica", "normal")
    doc.text(`Contratos con aumentos: ${contratos.length}`, 30, 130)
    const total = typeof totalAumentos === "number"
      ? totalAumentos
      : contratos.reduce((sum, c) => sum + (c.aumentos?.length || 0), 0)
    doc.text(`Total de aumentos aplicados: ${total}`, 30, 145)

    // Listado por contrato
    let y = 180
    const startY = 180

    contratos.forEach((c) => {
      // salto de página si no entra bloque mínimo
      if (y > 740) {
        doc.addPage()
        y = startY - 20
      }

      doc.setLineWidth(1)
        doc.line(30, y+3, 560, y+3)

      doc.setFont("helvetica", "bold")
      doc.setFontSize(12)
      const dirLines = doc.splitTextToSize(c.direccionInmueble, 400)
      dirLines.forEach((line, idx) => doc.text(line, 30, y + idx * 12))
      y += dirLines.length * 12

      doc.setFont("helvetica", "normal")
      doc.setFontSize(10)
      doc.text(`Locador: ${c.apellidoPropietario}, ${c.nombrePropietario}`, 30, y + 6)
      doc.text(`Locatario: ${c.apellidoInquilino}, ${c.nombreInquilino}`, 300, y + 6)
      y += 25

      // Encabezado de aumentos
      doc.setFont("helvetica", "bold")
      doc.setFontSize(9)
      doc.text("Período", 30, y)
      doc.text("Monto anterior", 150, y)
      doc.text("Monto nuevo", 280, y)
      doc.text("%", 410, y)
      y += 6
      doc.setLineWidth(0.3)
      doc.line(30, y, 560, y)
      y += 10

      doc.setFont("helvetica", "normal")
      doc.setFontSize(9)
      c.aumentos.forEach((a) => {
        if (y > 760) {
          doc.addPage()
          y = 60
          doc.setFont("helvetica", "bold")
          doc.setFontSize(9)
          doc.text("Mes", 30, y)
          doc.text("Monto anterior", 150, y)
          doc.text("Monto nuevo", 280, y)
          doc.text("%", 410, y)
          y += 6
          doc.line(30, y, 560, y)
          y += 10
          doc.setFont("helvetica", "normal")
          doc.setFontSize(9)
        }

        doc.text(formatoMesAnioLocal(a.fechaAumento), 30, y)
        doc.text(formatoMoneda(a.montoAnterior), 150, y)
        doc.text(formatoMoneda(a.montoNuevo), 280, y)
        doc.text(`${a.porcentajeAumento.toFixed(2)}%`, 410, y)
        y += 14
      })

      y += 20
    })

    // Pie
    doc.setTextColor(100, 100, 100)
    doc.setFontSize(9)
    doc.setFont("helvetica", "normal")
    doc.text("Reporte generado por el sistema AlquiGest.", 30, 815)
    doc.text("Gestione alquileres de forma simple.", 30, 828)

    const safeDesde = (periodoDesde || "").replace(/[^0-9A-Za-z_-]/g, "_")
    const safeHasta = (periodoHasta || "").replace(/[^0-9A-Za-z_-]/g, "_")
    const nombreArchivo = `Aumentos_${safeDesde}_${safeHasta}.pdf`
    doc.save(nombreArchivo)
  }

  const onClick = async () => {
    if (disabled) return
    if (onBeforeGenerate) {
      try { await onBeforeGenerate() } catch (e) { console.error("Error pre-PDF aumentos:", e) }
    }
    await generarPDF()
  }

  return (
    <Button onClick={onClick} disabled={disabled || !contratos.length} variant={"default"}>
      <FileDown className="h-4 w-4 mr-2" />
      Exportar PDF
    </Button>
  )
}
