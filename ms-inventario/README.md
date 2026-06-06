# MS Inventario - SMARTLOGIX

Microservicio de gestión de productos e inventario.

## Requisitos

- Java 17
- Maven 3.9+
- PostgreSQL 14+

## Base de datos

```sql
CREATE DATABASE inventario_db;
```

Configuración en `application.properties`:

- URL: `jdbc:postgresql://localhost:5432/inventario_db`
- Usuario: `postgres`
- Contraseña: `postgres`

## Instalación y ejecución

```bash
cd ms-inventario
./mvnw spring-boot:run
```

Puerto: **8081**

## Endpoints

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/productos` | Listar productos |
| GET | `/api/productos/{id}` | Obtener por id |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |
| POST | `/api/productos/{id}/descontar-stock` | Descontar stock |
| GET | `/api/productos/{id}/stock-suficiente/{cantidad}` | Validar stock |

## Swagger

http://localhost:8081/swagger-ui.html

## Pruebas

```bash
./mvnw test
```

Cobertura JaCoCo en `target/site/jacoco/index.html`.

## Patrones

- **Repository**: `ProductoRepository`
- **Builder**: `ProductoBuilder`
