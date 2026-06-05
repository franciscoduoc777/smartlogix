# BFF Gateway - SMARTLOGIX

Backend For Frontend en puerto **8080**.

## Ejecución

Requiere microservicios activos (8081, 8082, 8083).

```bash
cd bff-gateway
./mvnw spring-boot:run
```

## Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/dashboard` | Respuesta agregada |
| GET/POST/PUT/DELETE | `/api/productos` | Proxy inventario |
| GET/POST/PUT | `/api/pedidos` | Proxy pedidos |
| GET/POST/PUT | `/api/envios` | Proxy envíos |

## Patrón Facade

`LogisticaFacade` agrega datos sin lógica de negocio compleja.

## Swagger

http://localhost:8080/swagger-ui.html
