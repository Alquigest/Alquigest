import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../ui/card";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import ModalDefault from "../modal-default";
import ModalError from "../modal-error";
import { Label } from "../ui/label";
import { KeySquare, UserCheck2, Lock } from "lucide-react";
import { useRecuperarContrasena } from "@/hooks/useRecuperarContrasena";

export default function RecuperarContrasenaCodigos() {
    // Estados locales del formulario
    const [usuario, setUsuario] = useState("");
    const [codigo, setCodigo] = useState("");
    const [nuevaContrasena, setNuevaContrasena] = useState("");
    const [confirmarContrasena, setConfirmarContrasena] = useState("");
    
    // Hook de recuperación de contraseña
    const { 
        validarCodigo, 
        resetearContrasena, 
        loading, 
        error, 
        mensaje, 
        step,
        resetFlow 
    } = useRecuperarContrasena();

    // Paso 1: Validar código
    const handleValidarCodigo = async (e: React.FormEvent) => {
        e.preventDefault();
        
        try {
            await validarCodigo({ username: usuario, codigo });
        } catch (err) {
            // El error ya se maneja en el hook
            console.error("Error validando código:", err);
        }
    };

    // Paso 2: Resetear contraseña
    const handleResetearContrasena = async (e: React.FormEvent) => {
        e.preventDefault();
        
        // Validación en cliente
        if (nuevaContrasena !== confirmarContrasena) {
            return;
        }

        try {
            await resetearContrasena({ nuevaContrasena, confirmarContrasena });
        } catch (err) {
            // El error ya se maneja en el hook
            console.error("Error reseteando contraseña:", err);
        }
    };

    const handleVolverAIntentar = () => {
        resetFlow();
        setUsuario("");
        setCodigo("");
        setNuevaContrasena("");
        setConfirmarContrasena("");
    };
    return (
    <>
        <Card className="sm:w-4xl w-auto">
            <CardHeader>
                <CardTitle className="font-sans text-lg">
                {step === "validar-codigo" 
                    ? "Ingrese su nombre de usuario y código de recuperación para continuar"
                    : step === "nueva-contrasena"
                    ? "Ingrese su nueva contraseña"
                    : "Contraseña actualizada exitosamente"
                }
                </CardTitle>
                {step === "validar-codigo" && (
                    <CardDescription>
                        Utilice uno de los códigos de seguridad que recibió al crear su cuenta.
                    </CardDescription>
                )}
            </CardHeader>
            <CardContent className="flex items-center justify-center">
                {/* PASO 1: Validar código */}
                {step === "validar-codigo" && (
                    <form onSubmit={handleValidarCodigo} className="flex flex-col gap-4 w-2xl">
                        <div className="flex flex-col gap-5 w-full">
                            <div className="flex items-center gap-2 w-full">
                                <UserCheck2 />
                                <div className="w-full">
                                    <Label htmlFor="usuario">
                                        Nombre de usuario 
                                    </Label>
                                    <Input
                                        type="text"
                                        id="usuario"
                                        name="usuario"
                                        required
                                        value={usuario}
                                        onChange={(e) => setUsuario(e.target.value)}
                                        placeholder="Ingrese su nombre de usuario"
                                    />
                                </div>
                            </div>
                            <div className="flex items-center gap-2">
                                <KeySquare />
                                <div className="w-full">
                                    <Label htmlFor="codigo">
                                        Código de recuperación    
                                    </Label>
                                    <Input
                                        type="text"
                                        id="codigo"
                                        name="codigo"
                                        required
                                        value={codigo}
                                        onChange={(e) => setCodigo(e.target.value)}
                                        placeholder="Ingrese su código"
                                        title="Ingrese alguno de los código de recuperación de contraseña"
                                    />
                                </div>
                            </div>
                        </div>
                        <Button
                            type="submit"
                            loading={loading}
                        >
                            Validar código
                        </Button>
                    </form>
                )}

                {/* PASO 2: Nueva contraseña */}
                {step === "nueva-contrasena" && (
                    <form onSubmit={handleResetearContrasena} className="flex flex-col gap-4 w-2xl">
                        <div className="flex flex-col gap-5 w-full">
                            <div className="flex items-center gap-2 w-full">
                                <Lock />
                                <div className="w-full">
                                    <Label htmlFor="nuevaContrasena">
                                        Nueva contraseña
                                    </Label>
                                    <Input
                                        type="password"
                                        id="nuevaContrasena"
                                        name="nuevaContrasena"
                                        required
                                        minLength={6}
                                        value={nuevaContrasena}
                                        onChange={(e) => setNuevaContrasena(e.target.value)}
                                        placeholder="Ingrese su nueva contraseña"
                                    />
                                </div>
                            </div>
                            <div className="flex items-center gap-2">
                                <Lock />
                                <div className="w-full">
                                    <Label htmlFor="confirmarContrasena">
                                        Confirmar contraseña    
                                    </Label>
                                    <Input
                                        type="password"
                                        id="confirmarContrasena"
                                        name="confirmarContrasena"
                                        required
                                        minLength={6}
                                        value={confirmarContrasena}
                                        onChange={(e) => setConfirmarContrasena(e.target.value)}
                                        placeholder="Confirme su nueva contraseña"
                                    />
                                    {nuevaContrasena && confirmarContrasena && nuevaContrasena !== confirmarContrasena && (
                                        <p className="text-sm text-red-500 mt-1">
                                            Las contraseñas no coinciden
                                        </p>
                                    )}
                                </div>
                            </div>
                        </div>
                        <Button
                            type="submit"
                            loading={loading}
                            disabled={nuevaContrasena !== confirmarContrasena}
                        >
                            Cambiar contraseña
                        </Button>
                    </form>
                )}

                {/* PASO 3: Completado */}
                {step === "completado" && (
                    <div className="flex flex-col gap-4 w-2xl text-center">
                        <p className="text-green-600">
                            Su contraseña ha sido actualizada exitosamente.
                        </p>
                        <Button
                            onClick={() => window.location.href = "/"}
                        >
                            Ir a iniciar sesión
                        </Button>
                    </div>
                )}
            </CardContent>
        </Card>

        {/* Modal de error */}
        {error && (
            <ModalError
                titulo="Error"
                mensaje={error}
                onClose={() => handleVolverAIntentar()}
            />
        )}

        {/* Modal de éxito */}
        {mensaje && step === "completado" && (
            <ModalDefault
                titulo="Éxito"
                mensaje={mensaje}
                onClose={() => window.location.href = "/auth/login"}
            />
        )}
    </>
  )
}