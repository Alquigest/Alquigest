# 🏠 Alquigest Frontend

Interfaz de usuario para el sistema de gestión de alquileres a cargo del estudio jurídico.


## 📍 Estado actual

📂 Módulos / Funcionalidades disponibles

- Inmuebles
  - Consulta, edición, baja.
- Locadores
  - Consulta, edición, baja
- Locatarios
  - Consulta, edición, baja
- Pago de Servicios de un alquiler
  - Carga de un pago
  - Historial de pagos
- Gestión de Contratos
  - Alta, baja, gestión de recinsiones
  - Historial de contratos
  - Historial de pagos de alquiler
- Gestión de alquileres/contratos vigentes
- Gestión de notificaciones
- Gestión de Usuarios
  - Autenticación
  - Gestión de Permisos y roles
  - Recuperación de contraseña

 
## 📌 Próximas mejoras

- Dashboard con métricas y reportes.
- Integración con API de backend para servicios adicionales.
- Correción de errores/Performance


---

## 🚀 Tecnologías principales

- [Next.js 14](https://nextjs.org/) – Framework de React para renderizado híbrido (SSR/SSG).
- [TypeScript](https://www.typescriptlang.org/) – Tipado estático y mayor robustez.
- [TailwindCSS](https://tailwindcss.com/) – Estilos rápidos y personalizables.
- [Radix UI](https://www.radix-ui.com/) – Componentes accesibles de bajo nivel.
- [lucide-react](https://lucide.dev/) – Iconos personalizables.
- [react-hook-form](https://react-hook-form.com/) + [zod](https://zod.dev/) – Manejo y validación de formularios.


## ⚙️ Requisitos previos

Asegúrate de tener instalado en tu máquina:

- [Node.js](https://nodejs.org/) **>=18**
- [npm](https://www.npmjs.com/) **>=9**


## 🚀 Conexión con Backend

El frontend deberá consumir la API REST del backend que se ejecuta en:
- **URL Base**: `http://localhost:8081`
- **Documentación**: `http://localhost:8081/swagger-ui.html`


## 📦 Instalación y ejecución

Clona el repositorio e instala las dependencias:

```bash
git clone https://github.com/ConradoJuncos/Alquigest.git
cd frontend
npm install
npm run dev
```

Para construir el proyecto para producción:

```bash
npm run build
npm start
```
