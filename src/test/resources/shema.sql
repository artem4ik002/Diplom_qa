DROP TABLE IF EXISTS payment_entity;
CREATE TABLE payment_entity
(
    id       CHAR(36) PRIMARY KEY,
    status   VARCHAR(255)        NOT NULL
);

DROP TABLE IF EXISTS credit_request_entity;
CREATE TABLE credit_request_entity
(
    id       CHAR(36) PRIMARY KEY,
    status   VARCHAR(255)        NOT NULL
);