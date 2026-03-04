--liquibase formatted sql

--changeset dirijable:1
CREATE TABLE category
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(64) NOT NULL,
    is_deleted BOOLEAN DEFAULT false
);
--rollback DROP TABLE category;

--changeset dirijable:2
CREATE TABLE instructor
(
    id             BIGSERIAL PRIMARY KEY,
    first_name     VARCHAR(64)  NOT NULL,
    last_name      VARCHAR(64)  NOT NULL,
    description    VARCHAR(512) NOT NULL,
    specialization VARCHAR(128) NOT NULL
);
--rollback DROP TABLE instructor

--changeset dirijable:3
CREATE TABLE course
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(128)                    NOT NULL,
    description   VARCHAR(512)                    NOT NULL,
    instructor_id BIGINT                          REFERENCES instructor (id) ON DELETE SET NULL,
    category_id   BIGINT REFERENCES category (id) NOT NULL
);
--rollback DROP TABLE course;

CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(64) UNIQUE NOT NULL,
    password   TEXT               NOT NULL,
    first_name VARCHAR(64)        NOT NULL,
    last_name  VARCHAR(64)        NOT NULL,
    role       VARCHAR(32)        NOT NULL
);

--changeset dirijable:5
CREATE TABLE course_users
(
    course_id BIGINT REFERENCES course (id),
    user_id   BIGINT REFERENCES users (id),
    PRIMARY KEY (course_id, user_id)
);
--rollback DROP TABLE course_users

--changeset dirijable:6
CREATE TABLE lesson
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(128)                                    NOT NULL,
    content          VARCHAR(128)                                    NOT NULL,
    duration_minutes INT                                             NOT NULL CHECK (duration_minutes > 0),
    course_id        BIGINT REFERENCES course (id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT unique_title UNIQUE (course_id, title)
);
--rollback DELETE TABLE lesson

--changeset dirijable:7
CREATE TABLE refresh_token
(
    id              BIGSERIAL PRIMARY KEY,
    expiration_date TIMESTAMPTZ                          NOT NULL,
    token           VARCHAR(128)                         NOT NULL,
    user_email      VARCHAR(64) REFERENCES users (email) NOT NULL
);
--rollback DROP TABLE refresh_token