CREATE SEQUENCE wheels_id_seq;
CREATE TABLE wheels
(
    id    SERIAL PRIMARY KEY,
    brand VARCHAR(45) NOT NULL,
    name  VARCHAR(45) NOT NULL,

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
