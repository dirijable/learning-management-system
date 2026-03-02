--liquibase formatted sql

--changeset dirijable:1
CREATE TABLE category
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(64),
    is_deleted BOOLEAN
);
--rollback DROP TABLE category;

--changeset dirijable:2
CREATE TABLE instructor
(
    id             BIGSERIAL PRIMARY KEY,
    first_name     VARCHAR(64),
    last_name      VARCHAR(64),
    description    VARCHAR(512),
    specialization VARCHAR(128)
);
--rollback DROP TABLE instructor

--changeset dirijable:3
CREATE TABLE course
(

);
--rollback DROP TABLE course;

