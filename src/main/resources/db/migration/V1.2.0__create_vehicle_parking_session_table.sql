CREATE TABLE vehicle_parking_session
(
    id            SERIAL PRIMARY KEY,
    spot_id       INTEGER NULL,
    license_plate VARCHAR(20) NOT NULL,
    entry_time    TIMESTAMP   NOT NULL,
    parked_time   TIMESTAMP NULL,
    exit_time     TIMESTAMP NULL,
    status        VARCHAR(20) NOT NULL,
    total_price   NUMERIC(10, 2) NULL,
    created_at    TIMESTAMP   NOT NULL,
    updated_at    TIMESTAMP   NOT NULL,
    CONSTRAINT vehicle_parking_session_parking_spot_fk FOREIGN KEY (spot_id) REFERENCES parking_spot (id)
);