# Alquigest - Sistema de Gestión de Alquileres

Sistema completo para la gestión de alquileres de inmuebles, dividido en backend (API REST) y frontend (interfaz de usuario).

## Estructura del Proyecto

```
Alquigest/
├── backend/                    # API REST con Spring Boot
│   ├── src/main/java/com/alquileres
│   │   ├── /config
│   │   ├── /controller
│   │   ├── /dto
│   │   ├── /exception
│   │   ├── /model
│   │   ├── /repository
│   │   ├── /scheduler
│   │   ├── /security
│   │   ├── /security
│   │   ├── /util
│   │   └── /AlquigestApplication.java
│   ├── pom.xml
│   └── README.md
├── frontend/                   # Interfaz de usuario (por desarrollar)
│   ├── /app
│   ├── /components
│   ├── /contexts
│   ├── /hooks
│   ├── /lib
│   ├── /mocks
│   ├── /public
│   ├── /types
│   ├── /utils
│   └── README.md
└── README.md                   # Este archivo
```

## Tecnologías

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL** (Base de datos)
- **Maven** (Gestión de dependencias)
- **Swagger/OpenAPI** (Documentación de API)

### Frontend
- **TypeScript**
- **React 18 + Next.js 14**
- **NPM** (Gestión de dependencias)
- **TailwindCSS**

## Documentación

- **Backend**: Ver `backend/README.md`
- **Frontend**: Ver `frontend/README.md`

## Requisitos

### Backend
- **Java 21** o superior
- **Maven 3.6** o superior
- **Git**

### Frontend
- [Node.js](https://nodejs.org/) **>=18**
- [npm](https://www.npmjs.com/) **>=9**
