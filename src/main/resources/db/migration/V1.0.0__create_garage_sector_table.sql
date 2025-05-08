CREATE TABLE garage_sector
(
    id                     SERIAL PRIMARY KEY,
    sector_code            VARCHAR(10)    NOT NULL,
    base_price             NUMERIC(10, 2) NOT NULL,
    max_capacity           INTEGER        NOT NULL,
    open_hour              TIME           NOT NULL,
    close_hour             TIME           NOT NULL,
    duration_limit_minutes INTEGER        NOT NULL,
    available_spots        INTEGER        NOT NULL,
    created_at             TIMESTAMP      NOT NULL,
    updated_at             TIMESTAMP      NOT NULL
);