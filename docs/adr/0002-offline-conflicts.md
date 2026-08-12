# ADR 0002: Idempotent offline mutations

- Status: accepted
- Date: 2026-08-11

Every mobile mutation carries a unique client ID and the last server version it
observed. Replayed IDs return their original version. A stale expected version
returns `CONFLICT` plus current server state instead of overwriting newer work.
