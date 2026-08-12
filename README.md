# FieldOps Service Platform

Java 21/Spring Boot dispatch backend with PostGIS proximity search and
conflict-aware offline work-order synchronization.

## MVP

- Technician location upsert and nearest-available search in meters.
- Work-order creation, assignment and state transitions.
- Idempotent mobile mutations using `clientMutationId`.
- Optimistic version conflict responses for offline clients.
- Flyway, PostGIS, Testcontainers, Prometheus and CI.

Run `docker compose up -d` and `./gradlew bootRun`. The API uses port `8091`.
