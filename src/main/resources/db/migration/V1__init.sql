CREATE TABLE wheels
(
    id    SERIAL PRIMARY KEY,
    brand VARCHAR(45)        NOT NULL,
    name  VARCHAR(45) UNIQUE NOT NULL,

    UNIQUE (brand, name)
);

CREATE SEQUENCE wheels_id_seq;
