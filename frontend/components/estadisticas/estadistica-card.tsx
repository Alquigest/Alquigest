import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";


interface EstadisticaCardProps {
  titulo: string;
  valor?: string | number;
  icono: React.ReactNode;
  subtitulo?: string;
  tituloAyuda?: string;
}

export default function EstadisticaCard({ titulo, valor = "N/A", icono, subtitulo, tituloAyuda = "Alquigest S.A." }: EstadisticaCardProps) {
  return (
    <>
        <Card>
            <CardHeader title={tituloAyuda} className="flex flex-row items-center justify-between hover:cursor-help">
                <CardTitle className="text-md md:text-lg font-medium ">{titulo}</CardTitle>
                {icono}
            </CardHeader>
            <CardContent className="flex flex-col items-center">
                <div className="text-3xl font-bold font-sans text-foreground/80">{valor}</div>
                <p className="text-sm text-muted-foreground">{subtitulo}</p>
            </CardContent>
        </Card>
    </>
  );
}