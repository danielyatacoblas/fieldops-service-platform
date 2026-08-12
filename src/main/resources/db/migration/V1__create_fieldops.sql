CREATE EXTENSION IF NOT EXISTS postgis;
CREATE TABLE technicians (
 id UUID PRIMARY KEY, name VARCHAR(120) NOT NULL, latitude DOUBLE PRECISION NOT NULL,
 longitude DOUBLE PRECISION NOT NULL, available BOOLEAN NOT NULL, updated_at TIMESTAMPTZ NOT NULL,
 location GEOGRAPHY(Point,4326) GENERATED ALWAYS AS (ST_SetSRID(ST_MakePoint(longitude,latitude),4326)::geography) STORED,
 CHECK(latitude BETWEEN -90 AND 90), CHECK(longitude BETWEEN -180 AND 180)
);
CREATE INDEX idx_technicians_location ON technicians USING GIST(location);
CREATE TABLE work_orders (
 id UUID PRIMARY KEY, summary VARCHAR(200) NOT NULL, status VARCHAR(30) NOT NULL,
 technician_id UUID REFERENCES technicians(id), latitude DOUBLE PRECISION NOT NULL, longitude DOUBLE PRECISION NOT NULL,
 version BIGINT NOT NULL DEFAULT 0, created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL
);
CREATE TABLE applied_mutations (
 client_mutation_id UUID PRIMARY KEY, work_order_id UUID NOT NULL REFERENCES work_orders(id),
 result_version BIGINT NOT NULL, applied_at TIMESTAMPTZ NOT NULL
);
