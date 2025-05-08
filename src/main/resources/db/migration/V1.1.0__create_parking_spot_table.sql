CREATE TABLE parking_spot
(
    id         SERIAL PRIMARY KEY,
    sector_id  INTEGER          NOT NULL,
    lat        DOUBLE PRECISION NOT NULL,
    lng        DOUBLE PRECISION NOT NULL,
    occupied   BOOLEAN          NOT NULL,
    created_at TIMESTAMP        NOT NULL,
    updated_at TIMESTAMP        NOT NULL,
    CONSTRAINT parking_spot_garage_sector_fk FOREIGN KEY (sector_id) REFERENCES garage_sector (id)
);