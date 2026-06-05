# MS Envíos - SMARTLOGIX

Microservicio de gestión de envíos logísticos.

## Requisitos

- Java 17, Maven, PostgreSQL
- **ms-pedidos** en puerto 8082 (pedido aprobado)

## Base de datos

```sql
CREATE DATABASE envios_db;
```

## Ejecución

```bash
cd ms-envios
./mvnw spring-boot:run
```

Puerto: **8083**

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/envios` | Listar |
| GET | `/api/envios/{id}` | Obtener |
| POST | `/api/envios` | Crear (solo pedido aprobado) |
| PUT | `/api/envios/{id}/estado?estado=` | Cambiar estado |
| DELETE | `/api/envios/{id}` | Eliminar |

## Patrones

- **Repository**: `EnvioRepository`

## Pruebas

```bash
./mvnw test
```
