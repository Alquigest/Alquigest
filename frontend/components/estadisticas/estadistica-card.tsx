import { Card, CardContent, CardHeader, CardTitle } from "../ui/card";
import { Skeleton } from "../ui/skeleton";


interface EstadisticaCardProps {
  titulo: string;
  valor?: string | number;
  icono: React.ReactNode;
  subtitulo?: string;
  tituloAyuda?: string;
  cargando?: boolean;
}

export default function EstadisticaCard({ titulo, valor = "N/A", icono, subtitulo, tituloAyuda = "Alquigest S.A.", cargando=false }: EstadisticaCardProps) {
  return (
    <Card>
      <CardHeader title={tituloAyuda} className="flex flex-row items-center justify-between hover:cursor-help">
        {cargando ? (
          <>
            <Skeleton className="h-5 w-32" />
            <Skeleton className="h-6 w-6 rounded-full" />
          </>
        ) : (
          <>
            <CardTitle className="text-md md:text-lg font-medium ">{titulo}</CardTitle>
            {icono}
          </>
        )}
      </CardHeader>
      <CardContent className="flex flex-col items-center">
        {cargando ? (
          <>
            <Skeleton className="h-8 w-20 mb-2" />
            {subtitulo ? <Skeleton className="h-3 w-40" /> : null}
          </>
        ) : (
          <>
            <div className="text-3xl font-bold font-sans text-foreground/80">{valor}</div>
            {subtitulo && <p className="text-sm text-muted-foreground">{subtitulo}</p>}
          </>
        )}
      </CardContent>
    </Card>
  );
}