# FieldOps Service Platform

## Objetivo del proyecto

Demostrar el backend y la operación visual de una solución de servicio en
campo: asignación geoespacial, SLA, técnicos y sincronización offline con
resolución de conflictos. Combina Java 21, Spring Boot, PostGIS y una consola
React responsiva orientada al despacho.

## Evidencia visual

![Consola de despacho FieldOps](docs/screenshots/dashboard-desktop.png)

| Arquitectura | Flujo funcional | GitFlow |
|---|---|---|
| ![Arquitectura](diagrams/rendered/architecture.svg) | ![Flujo](diagrams/rendered/flow.svg) | ![GitFlow](diagrams/rendered/gitflow.svg) |

Disponible también la [vista móvil](docs/screenshots/dashboard-mobile.png).

## Consola web

```bash
cd frontend
npm ci
npm run dev
```

La UI abre en `http://localhost:5175` y enruta la API hacia
`http://localhost:8091`.

Java 21/Spring Boot dispatch backend with PostGIS proximity search and
conflict-aware offline work-order synchronization.

## MVP

- Technician location upsert and nearest-available search in meters.
- Work-order creation, assignment and state transitions.
- Idempotent mobile mutations using `clientMutationId`.
- Optimistic version conflict responses for offline clients.
- Flyway, PostGIS, Testcontainers, Prometheus and CI.

Run `docker compose up -d` and `./gradlew bootRun`. The API uses port `8091`.
