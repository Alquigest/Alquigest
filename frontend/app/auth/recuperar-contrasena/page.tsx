"use client";
import RecuperarContrasenaPaso1 from "@/components/contrasenas/recuperar-contrasena-card";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle } from "@/components/ui/card";
import { ArrowLeft, KeyRound } from "lucide-react";

export default function RecuperarContrasenaPage() {
  return (
    <main className="container h-screen mx-auto px-6 py-8 pt-30">
        <div className="flex flex-col gap-3 mb-8">
            <Button variant="outline" onClick={() => window.history.back()} className="w-fit">
                <ArrowLeft className="h-4 w-4 mr-2" />
                Volver
            </Button>
            <div className="flex items-center m-5 gap-2">
                <KeyRound className="h-11 w-11" />
                <h1 className="text-3xl font-bold">Recupera tu contraseña</h1>
            </div>
        </div>
        <div className="flex items-center justify-center">
            <RecuperarContrasenaPaso1 />
        </div>
    </main>
  );
}
