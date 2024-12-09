CREATE SEQUENCE cities_id_seq;
CREATE TABLE cities
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(45) NOT NULL,
    province VARCHAR(45) NOT NULL,

    UNIQUE (name)
);

-- Security
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(45) UNIQUE NOT NULL,
    password VARCHAR(255)       NOT NULL,
    enabled  BOOLEAN            NOT NULL DEFAULT TRUE,

    UNIQUE (username)
);

CREATE TABLE authorities
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(45) NOT NULL,
    authority VARCHAR(45) NOT NULL,

    UNIQUE (authority, username),
    FOREIGN KEY (username) REFERENCES users (username)
);
