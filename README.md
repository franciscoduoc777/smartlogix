# SMARTLOGIX

Plataforma logística académica basada en **microservicios**, arquitectura **MVC** en cada backend, **BFF** y frontend **React**.

## Estructura del monorepo

```
smartlogix/
├── ms-inventario/     # Puerto 8081 - inventario_db
├── ms-pedidos/        # Puerto 8082 - pedidos_db
├── ms-envios/         # Puerto 8083 - envios_db
├── bff-gateway/       # Puerto 8080
├── frontend-web/      # Puerto 5173
└── docs/
```

## Requisitos

- Java 17, Maven 3.9+
- PostgreSQL 14+
- Node.js 18+ (frontend)

## Bases de datos

Ejecutar `docs/init-databases.sql` en PostgreSQL o crear manualmente:

- `inventario_db`
- `pedidos_db`
- `envios_db`

Credenciales por defecto: usuario `postgres`, contraseña `postgres` (ajustar en cada `application.properties`).

## Orden de ejecución

1. `ms-inventario` → `./mvnw spring-boot:run`
2. `ms-pedidos` → `./mvnw spring-boot:run`
3. `ms-envios` → `./mvnw spring-boot:run`
4. `bff-gateway` → `./mvnw spring-boot:run`
5. `frontend-web` → `npm install && npm run dev`

## Patrones implementados

| Patrón | Ubicación |
|--------|-----------|
| Repository | Repositorios JPA en cada MS |
| Builder | `ProductoBuilder` (ms-inventario) |
| Factory Method | `PedidoFactory` (ms-pedidos) |
| Facade | `InventarioFacade`, `LogisticaFacade` |

## Pruebas unitarias

```bash
cd ms-inventario && ./mvnw test
cd ms-pedidos && ./mvnw test
cd ms-envios && ./mvnw test
```

JaCoCo genera reporte de cobertura en `target/site/jacoco/`.

## Swagger

- Inventario: http://localhost:8081/swagger-ui.html
- Pedidos: http://localhost:8082/swagger-ui.html
- Envíos: http://localhost:8083/swagger-ui.html
- BFF: http://localhost:8080/swagger-ui.html

## GitFlow

Ver `docs/GITFLOW.md` y `docs/plan-branching.pdf`.

## Documentación académica

- `docs/analisis-patrones.pdf` — justificación de patrones y microservicios
- `docs/plan-branching.pdf` — estrategia de ramas

Generar PDFs: `python docs/generar-pdfs.py` (requiere `fpdf2`: `pip install fpdf2`).
