# Frontend Web - SMARTLOGIX

Aplicación React que consume **únicamente el BFF** (`http://localhost:8080`).

## Instalación

```bash
cd frontend-web
npm install
```

## Ejecución

```bash
npm run dev
```

URL: http://localhost:5173

## Pantallas

- Dashboard (agregado `/api/dashboard`)
- Inventario (CRUD productos vía BFF)
- Pedidos (crear, aprobar, rechazar)
- Envíos (crear y cambiar estado)

## Componentes reutilizables

- `Navbar`, `TablaProductos`, `FormularioProducto`
- `TablaPedidos`, `FormularioPedido`, `TablaEnvios`, `FormularioEnvio`

## Validaciones y errores

Formularios con campos obligatorios y mensajes toast de éxito/error.
