# Documentación: Control de Aumentos Manuales para Alquileres

## Resumen

Se ha implementado un sistema para manejar fallos en la API del BCRA durante el cálculo de aumentos automáticos de alquileres. Cuando la API falla, el alquiler se crea con el monto base y se marca para que el usuario pueda ingresar manualmente los índices ICL.

## Cambios Realizados

### 1. Modelo de Datos

#### Alquiler.java
- **Nuevo campo**: `necesitaAumentoManual` (Boolean, default: false)
  - Indica si el alquiler requiere que el usuario ingrese manualmente los índices ICL
  - Se marca como `true` cuando falla la consulta a la API del BCRA

```java
@Column(name = "necesita_aumento_manual", nullable = false)
private Boolean necesitaAumentoManual = false;
```

### 2. DTOs

#### IndicesICLManualDTO.java (NUEVO)
DTO para recibir índices ICL manuales del usuario:

```java
public class IndicesICLManualDTO {
    @NotNull
    @Positive
    private BigDecimal indiceInicial;
    
    @NotNull
    @Positive
    private BigDecimal indiceFinal;
    
    public BigDecimal calcularTasaAumento() {
        return indiceFinal.divide(indiceInicial, 10, BigDecimal.ROUND_HALF_UP);
    }
}
```

#### AlquilerDTO.java
- **Nuevo campo**: `necesitaAumentoManual`
  - Se incluye en el DTO para que el frontend pueda identificar alquileres pendientes de aumento manual

### 3. Servicios

#### AlquilerActualizacionService.java
**Modificación en la creación automática de alquileres**:

Cuando falla la API del BCRA (contratos con `aumentaConICL = true`):
1. Se crea el alquiler con el monto base (sin aumento)
2. Se marca `necesitaAumentoManual = true`
3. Se registra un log de advertencia
4. El proceso continúa sin interrumpirse

```java
try {
    // Intentar obtener tasa de la API del BCRA
    BigDecimal tasaAumento = bcraApiClient.obtenerTasaAumentoICL(fechaInicio, fechaFin);
    // ... aplicar aumento
} catch (Exception e) {
    logger.error("Error al consultar ICL para contrato ID {}: {}. Se marcará para aumento manual.",
                 contrato.getId(), e.getMessage());
    
    // Crear alquiler con monto base y marcar para aumento manual
    Alquiler alquilerConError = new Alquiler(contrato, fechaVencimientoISO, montoBase);
    alquilerConError.setEsActivo(true);
    alquilerConError.setNecesitaAumentoManual(true);
    nuevosAlquileres.add(alquilerConError);
    
    logger.warn("Alquiler para contrato ID {} creado con necesitaAumentoManual=true", contrato.getId());
    continue;
}
```

#### AlquilerService.java
**Nuevos métodos**:

1. **`aplicarAumentoManual(alquilerId, indiceInicial, indiceFinal)`**
   - Aplica un aumento manual usando índices ICL proporcionados por el usuario
   - Valida que el alquiler exista y esté marcado con `necesitaAumentoManual = true`
   - Calcula el nuevo monto: `montoAnterior * (indiceFinal / indiceInicial)`
   - Actualiza el alquiler y marca `necesitaAumentoManual = false`
   - Registra el aumento en el historial

2. **`obtenerAlquileresConAumentoManualPendiente()`**
   - Devuelve todos los alquileres activos que necesitan aumento manual
   - Útil para mostrar una lista de pendientes al usuario

#### AumentoAlquilerService.java
**Nuevo método**:

- **`crearYGuardarAumento(contrato, montoAnterior, montoNuevo, porcentajeAumento)`**
  - Crea y guarda un registro de aumento en el historial
  - Utilizado por el proceso de aumento manual

### 4. Repositorio

#### AlquilerRepository.java
**Nueva query**:

```java
@Query("SELECT a FROM Alquiler a WHERE a.necesitaAumentoManual = true AND a.esActivo = true")
List<Alquiler> findByNecesitaAumentoManualTrueAndEsActivoTrue();
```

### 5. Controladores

#### AlquilerController.java
**Nuevos endpoints**:

1. **GET `/api/alquileres/aumento-manual/pendientes`**
   - Devuelve lista de alquileres que necesitan aumento manual
   - Response: `List<AlquilerDTO>`

2. **POST `/api/alquileres/{id}/aumento-manual`**
   - Aplica aumento manual a un alquiler específico
   - Request Body:
     ```json
     {
       "indiceInicial": 28.140000,
       "indiceFinal": 28.190000
     }
     ```
   - Response: `AlquilerDTO` actualizado

## Flujo de Trabajo

### Escenario: Falla la API del BCRA

1. **Al inicio de mes (automático)**:
   - El sistema intenta crear alquileres para contratos vigentes
   - Para contratos con `aumentaConICL = true`, intenta obtener índices de la API del BCRA
   - Si la API falla:
     - Crea el alquiler con el monto del mes anterior
     - Marca `necesitaAumentoManual = true`
     - Continúa procesando los demás alquileres

2. **Usuario verifica pendientes**:
   ```
   GET /api/alquileres/aumento-manual/pendientes
   ```
   - Obtiene lista de alquileres que necesitan aumento manual

3. **Usuario busca índices ICL manualmente**:
   - Accede a la API del BCRA directamente o desde otra fuente
   - URL: `https://api.bcra.gob.ar/estadisticas/v4.0/monetarias/40?desde=yyyy-mm-dd&hasta=yyyy-mm-dd`
   - Obtiene los valores de `fechaInicio` y `fechaFin`

4. **Usuario aplica aumento manual**:
   ```
   POST /api/alquileres/{alquilerId}/aumento-manual
   {
     "indiceInicial": 28.140000,
     "indiceFinal": 28.190000
   }
   ```
   - El sistema calcula: `nuevoMonto = montoAnterior * (indiceFinal / indiceInicial)`
   - Actualiza el alquiler
   - Registra el aumento en el historial
   - Marca `necesitaAumentoManual = false`

## Ejemplo de Uso

### Request para aplicar aumento manual:
```http
POST /api/alquileres/123/aumento-manual
Content-Type: application/json

{
  "indiceInicial": 28.140000,
  "indiceFinal": 28.190000
}
```

### Response:
```json
{
  "id": 123,
  "contratoId": 45,
  "fechaVencimientoPago": "2025-11-10",
  "monto": 150266.82,
  "estaPagado": false,
  "necesitaAumentoManual": false,
  "inmuebleId": 10,
  "direccionInmueble": "Av. Corrientes 1234",
  "inquilinoId": 5,
  "nombreInquilino": "Juan",
  "apellidoInquilino": "Pérez"
}
```

### Cálculo del aumento:
- Índice inicial: 28.140000
- Índice final: 28.190000
- Tasa de aumento: 28.190000 / 28.140000 = 1.00177683
- Monto anterior: $150,000
- Nuevo monto: $150,000 * 1.00177683 = $150,266.82
- Porcentaje de aumento: 0.18%

## Validaciones

El sistema valida:
1. El alquiler existe
2. El alquiler está marcado con `necesitaAumentoManual = true`
3. Los índices ICL son mayores a cero
4. Los índices ICL son válidos (no nulos)

## Logs

El sistema registra:
- Cuando falla la API del BCRA
- Cuando se crea un alquiler con `necesitaAumentoManual = true`
- Cuando se aplica un aumento manual
- Los cálculos realizados (tasa, porcentaje, montos)

## Ventajas de esta Implementación

1. **Robustez**: El sistema no se interrumpe si falla la API del BCRA
2. **Transparencia**: El usuario puede ver qué alquileres necesitan ajuste manual
3. **Trazabilidad**: Todos los aumentos (automáticos y manuales) se registran en el historial
4. **Flexibilidad**: El usuario puede obtener los índices de fuentes alternativas si la API falla
5. **Consistencia**: El cálculo manual usa la misma fórmula que el automático

## Consideraciones para el Frontend

1. Mostrar un indicador visual para alquileres con `necesitaAumentoManual = true`
2. Proveer un formulario para ingresar índices ICL
3. Mostrar instrucciones sobre dónde obtener los índices ICL
4. Incluir un enlace a la API del BCRA para facilitar la búsqueda
5. Mostrar el cálculo antes de confirmar el aumento

## Migraciones de Base de Datos

Al desplegar en producción, ejecutar:

```sql
ALTER TABLE alquileres 
ADD COLUMN necesita_aumento_manual BOOLEAN NOT NULL DEFAULT false;
```

## Testing

Se recomienda crear tests para:
1. Creación de alquiler cuando falla la API del BCRA
2. Aplicación de aumento manual con índices válidos
3. Validación de alquileres sin marca de aumento manual
4. Validación de índices ICL inválidos
5. Registro de aumentos en el historial

