# ADR 0001: PostGIS geography proximity

- Status: accepted
- Date: 2026-08-11

Technician coordinates generate a PostGIS `geography(Point,4326)` column with a
GiST index. Dispatch queries use `ST_DWithin` for indexed radius filtering and
`ST_Distance` for meter-accurate ordering.
