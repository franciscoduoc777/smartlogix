# MS Pedidos - SMARTLOGIX

Microservicio de gestión de pedidos con integración a inventario.

## Requisitos

- Java 17, Maven, PostgreSQL
- **ms-inventario** ejecutándose en puerto 8081

## Base de datos

```sql
CREATE DATABASE pedidos_db;
```

## Ejecución

```bash
cd ms-pedidos
./mvnw spring-boot:run
```

Puerto: **8082**

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/pedidos` | Listar |
| GET | `/api/pedidos/{id}` | Obtener |
| POST | `/api/pedidos` | Crear |
| PUT | `/api/pedidos/{id}/aprobar` | Aprobar (valida stock) |
| PUT | `/api/pedidos/{id}/rechazar` | Rechazar |
| PUT | `/api/pedidos/{id}/preparacion` | En preparación |
| GET | `/api/pedidos/{id}/aprobado` | Verificar aprobación |

## Patrones

- **Repository**: `PedidoRepository`
- **Factory Method**: `PedidoFactory`
- **Facade**: `InventarioFacade`

## Pruebas

```bash
./mvnw test
```
