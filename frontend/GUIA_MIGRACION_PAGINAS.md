# 📚 Guía de Migración de Páginas - Cookies HttpOnly

## 🎯 Objetivo
Esta guía te ayudará a migrar cada página de la aplicación para usar el nuevo sistema de autenticación con cookies HttpOnly.

## 📋 Ejemplo: Página de Inquilinos (Ya migrada)

### Cambios Aplicados

#### 1. **Imports - Eliminar referencias antiguas**

```typescript
// ❌ ANTES - Eliminar estos imports
import { fetchWithToken } from "@/utils/functions/auth-functions/fetchWithToken"
import auth from "@/utils/functions/auth-functions/auth"
import BACKEND_URL from "@/utils/backendURL"

// ✅ DESPUÉS - Usar estos imports
import { fetchWithCredentials } from "@/utils/functions/fetchWithCredentials"
import { useAuth } from "@/contexts/AuthProvider"
```

#### 2. **Hook de Autenticación - Al inicio del componente**

```typescript
export default function MiPagina() {
  // ✅ AGREGAR: Obtener funciones de autenticación
  const { hasPermission, hasRole, user } = useAuth();
  
  // ... resto del código
}
```

**Funciones disponibles:**
- `hasPermission(permiso)` - Verifica si el usuario tiene un permiso específico
- `hasRole(rol)` - Verifica si el usuario tiene un rol específico
- `user` - Objeto con información del usuario (username, email, roles, permisos)
- `isAuthenticated` - Boolean si hay sesión activa
- `logout()` - Función para cerrar sesión

#### 3. **Peticiones GET - Reemplazar fetchWithToken**

```typescript
// ❌ ANTES
const data = await fetchWithToken(`${BACKEND_URL}/inquilinos/activos`);

// ✅ DESPUÉS - Opción 1: Con fetchWithCredentials (más control)
const response = await fetchWithCredentials('/inquilinos/activos');
const data = await response.json();

// ✅ DESPUÉS - Opción 2: Con fetchJSON (más simple, recomendado)
import { fetchJSON } from "@/utils/functions/fetchWithCredentials"
const data = await fetchJSON<TipoEsperado>('/inquilinos/activos');
```

**Ventajas de usar fetchJSON:**
- Parsea el JSON automáticamente
- Maneja errores automáticamente
- Más conciso y legible
- Type-safe con TypeScript

#### 4. **Peticiones POST/PUT/DELETE - Actualizar**

```typescript
// ❌ ANTES
const response = await fetchWithToken(
  `${BACKEND_URL}/inquilinos/${id}`,
  {
    method: "PUT",
    body: JSON.stringify(data),
  }
);

// ✅ DESPUÉS - Opción 1: fetchWithCredentials
const response = await fetchWithCredentials(
  `/inquilinos/${id}`,
  {
    method: "PUT",
    body: JSON.stringify(data),
  }
);
const updatedData = await response.json();

// ✅ DESPUÉS - Opción 2: fetchMutation (recomendado)
import { fetchMutation } from "@/utils/functions/fetchWithCredentials"
const updatedData = await fetchMutation('/inquilinos/${id}', 'PUT', data);
```

#### 5. **Verificación de Permisos - Actualizar**

```typescript
// ❌ ANTES
disabled={!auth.tienePermiso("modificar_inquilino")}

// ✅ DESPUÉS
disabled={!hasPermission("modificar_inquilino")}
```

```typescript
// ❌ ANTES
if (auth.hasRol("ROLE_ADMINISTRADOR")) {
  // hacer algo
}

// ✅ DESPUÉS
if (hasRole("ROLE_ADMINISTRADOR")) {
  // hacer algo
}
```

#### 6. **Acceso a información del usuario**

```typescript
// ❌ ANTES
const userData = auth.getUser();
const username = userData?.username;

// ✅ DESPUÉS
const { user } = useAuth();
const username = user?.username;
```

## 🔄 Patrón de Migración Completo

### Plantilla para Migrar una Página

```typescript
"use client"

// 1️⃣ IMPORTS - Actualizar
import { useAuth } from "@/contexts/AuthProvider";
import { fetchWithCredentials, fetchJSON, fetchMutation } from "@/utils/functions/fetchWithCredentials";
// Eliminar: import auth from "...auth"
// Eliminar: import { fetchWithToken } from "..."
// Eliminar: import BACKEND_URL from "..."

export default function MiPagina() {
  // 2️⃣ HOOK DE AUTH - Agregar al inicio
  const { hasPermission, hasRole, user } = useAuth();
  
  // 3️⃣ PETICIONES - Actualizar en useEffect y funciones
  useEffect(() => {
    const fetchData = async () => {
      try {
        // ✅ Nuevo método
        const data = await fetchJSON<MiTipo>('/mi-endpoint');
        setData(data);
      } catch (error) {
        console.error("Error:", error);
      }
    };
    fetchData();
  }, []);

  // 4️⃣ MUTACIONES - Actualizar en handlers
  const handleUpdate = async (id: number, newData: any) => {
    try {
      // ✅ Nuevo método
      const updated = await fetchMutation(`/mi-endpoint/${id}`, 'PUT', newData);
      // actualizar estado...
    } catch (error) {
      console.error("Error:", error);
    }
  };

  // 5️⃣ PERMISOS - Actualizar en renderizado
  return (
    <div>
      <Button 
        disabled={!hasPermission("mi_permiso")}
        onClick={handleAction}
      >
        Acción
      </Button>
      
      {hasRole("ROLE_ADMINISTRADOR") && (
        <div>Contenido solo para admin</div>
      )}
    </div>
  );
}
```

## 📝 Checklist por Página

Para cada página que migres, verifica:

- [ ] ❌ Eliminar `import auth from "..."`
- [ ] ❌ Eliminar `import { fetchWithToken } from "..."`
- [ ] ❌ Eliminar `import BACKEND_URL from "..."`
- [ ] ✅ Agregar `import { useAuth } from "@/contexts/AuthProvider"`
- [ ] ✅ Agregar `import { fetchWithCredentials/fetchJSON } from "..."`
- [ ] ✅ Agregar `const { hasPermission, hasRole, user } = useAuth()`
- [ ] ✅ Reemplazar todos los `fetchWithToken()` por `fetchWithCredentials()` o `fetchJSON()`
- [ ] ✅ Reemplazar todas las URLs `${BACKEND_URL}/ruta` por `/ruta`
- [ ] ✅ Reemplazar `auth.tienePermiso()` por `hasPermission()`
- [ ] ✅ Reemplazar `auth.hasRol()` por `hasRole()`
- [ ] ✅ Reemplazar `auth.getUser()` por `user`
- [ ] ✅ Probar la página (login, permisos, peticiones)

## 🔍 Buscar y Reemplazar

### Buscar páginas que necesitan migración

```bash
# Desde la carpeta frontend/
grep -r "fetchWithToken" app/
grep -r "auth.tienePermiso" app/
grep -r "auth.hasRol" app/
grep -r "auth.getUser" app/
grep -r "BACKEND_URL" app/
```

### Patrón de reemplazo común

| Buscar | Reemplazar por |
|--------|----------------|
| `import { fetchWithToken }...` | `import { fetchWithCredentials, fetchJSON }...` |
| `import auth from...` | `import { useAuth } from "@/contexts/AuthProvider"` |
| `${BACKEND_URL}/endpoint` | `/endpoint` |
| `fetchWithToken(...)` | `fetchWithCredentials(...)` o `fetchJSON<Type>(...)` |
| `auth.tienePermiso(` | `hasPermission(` |
| `auth.hasRol(` | `hasRole(` |
| `auth.getUser()` | `user` |

## 🚨 Casos Especiales

### 1. Peticiones con respuesta 204 (No Content)

```typescript
// Si el endpoint retorna 204 sin body
const response = await fetchWithCredentials('/endpoint', { method: 'DELETE' });
if (response.status === 204) {
  // No hay body, manejar directamente
  console.log("Eliminado exitosamente");
}
```

### 2. Descargas de archivos

```typescript
const response = await fetchWithCredentials('/download/pdf');
const blob = await response.blob();
const url = window.URL.createObjectURL(blob);
// usar url para descargar...
```

### 3. Upload de archivos

```typescript
const formData = new FormData();
formData.append('file', file);

const response = await fetchWithCredentials('/upload', {
  method: 'POST',
  body: formData,
  // NO incluir Content-Type, se configura automáticamente
  headers: {} // Sobrescribe el Content-Type por defecto
});
```

### 4. Verificar múltiples permisos

```typescript
const { hasPermission } = useAuth();

const canEdit = hasPermission("modificar_inquilino");
const canDelete = hasPermission("eliminar_inquilino");
const canView = hasPermission("ver_inquilino");

if (canEdit && canDelete) {
  // usuario tiene ambos permisos
}
```

## 📂 Archivos Típicos a Migrar

### Páginas de Listado
- `app/propietarios/page.tsx`
- `app/inmuebles/page.tsx`
- `app/inquilinos/page.tsx` ✅ (Ya migrada)
- `app/contratos/page.tsx`
- `app/alquileres/page.tsx`
- `app/pago-servicios/page.tsx`

### Páginas de Detalle
- `app/propietarios/[id]/page.tsx`
- `app/inmuebles/[id]/page.tsx`
- `app/inquilinos/[id]/page.tsx`
- `app/contratos/[id]/page.tsx`

### Modales y Componentes
- `components/**/*.tsx` (cualquier componente que use auth o fetchWithToken)

## 🧪 Testing de Cada Página

Después de migrar cada página, probar:

1. **Cargar la página** → debe mostrar datos correctamente
2. **Crear registro** → debe funcionar sin errores
3. **Editar registro** → debe actualizar correctamente
4. **Eliminar registro** → debe eliminar (si aplica)
5. **Permisos** → botones deshabilitados si no tiene permisos
6. **Cerrar sesión y recargar** → debe redirigir al login

## ✨ Beneficios Después de Migrar

- ✅ **Más seguro**: Token no expuesto en JavaScript
- ✅ **Más simple**: No manipular localStorage manualmente
- ✅ **Más limpio**: Código más legible y mantenible
- ✅ **Centralizado**: Un solo lugar para manejar auth
- ✅ **Type-safe**: Mejor soporte de TypeScript

## 📞 Ayuda

Si encuentras problemas:
1. Revisar la consola del navegador
2. Verificar la pestaña Network en DevTools
3. Comprobar que la cookie se está enviando
4. Verificar que el backend responde correctamente

---

**Ejemplo completo**: Ver `app/inquilinos/page.tsx` para referencia
