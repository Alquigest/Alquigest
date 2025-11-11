## Cosas para hacer:

### SPRINT 5 FIX
- [X] Clave fiscal mostrar en el detalla propietario solo si es abogada
- [x] Mostrar estado contrato actual
- [ ] Agregar en back y front el porcentaje de mora (1%)
- [x] Agregar la posibilidad de agregar un nuevo servicio al contrato
- [x] Utilizar endpoint tiene contratos vigentes para mostrar contratos de un inmueble
- [x] Mercedes Locativas, se muestra para contratos no vigenetes.
- [ ] Periodo de aumento me deja ingresar un número mayor que el posible
- [x] Inquilino FetchWithCredentials cambia por FetchWitchToken
- [x] Porcentaje fijo, no permite crear un contrato de ese estilo
- [x] Pago masivo en servicios

### MEJORAS EN SPRINT 4 5
- [x] Búsqueda en todas las pages importantes
  - [x] Componente SearchBar que reciba por parámetro:
    - 1 el array de objetcs
    - 2 una función callback que le permita al componente padre (la page), setear el array de objetcs filtrados para que la page sea la encargada de mostrar los objetos.


  #### ENDPOINTS
  - [ ] En alquileres agregar fecha de pago (registrar la fecha de pago, es decir, enviarla y al momento del get también traerla para mostrarla en la tabla.)
    - [ ] Puede haber seteo automático de la fecha en caso de alquileres viejos (en vez de la fecha mostrar "Automático").
