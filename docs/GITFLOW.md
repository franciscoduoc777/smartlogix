# GitFlow - SMARTLOGIX

## Ramas principales

| Rama | Propósito |
|------|-----------|
| `main` | Código en producción / entrega final |
| `develop` | Integración continua de features |

## Ramas de feature

- `feature/inventario` — ms-inventario
- `feature/pedidos` — ms-pedidos
- `feature/envios` — ms-envios
- `feature/bff` — bff-gateway
- `feature/frontend` — frontend-web

## Flujo de trabajo

1. Crear rama desde `develop`: `git checkout -b feature/inventario develop`
2. Commits atómicos en la feature
3. **Pull Request** hacia `develop`
4. Revisión de código y pruebas (`mvn test`)
5. **Merge** a `develop` (squash o merge commit según política del equipo)
6. Release: merge `develop` → `main` con tag de versión

## Resolución de conflictos

1. Actualizar la rama feature: `git fetch origin && git rebase origin/develop`
2. Resolver archivos en conflicto manualmente
3. `git add .` y `git rebase --continue`
4. Ejecutar pruebas antes de push
5. Actualizar el PR en la plataforma Git

## Pull Request

- Título descriptivo: `[feature/inventario] CRUD productos y pruebas`
- Descripción: qué cambia, cómo probar, capturas si aplica
- Requiere al menos una revisión antes del merge
