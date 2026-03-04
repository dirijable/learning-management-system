--liquibase formatted sql

--changeset dirijable:1
CREATE TABLE category
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT false
);
--rollback DROP TABLE category;

--changeset dirijable:2
CREATE TABLE instructor
(
    id             BIGSERIAL PRIMARY KEY,
    first_name     TEXT  NOT NULL,
    last_name      TEXT  NOT NULL,
    description    TEXT NOT NULL,
    specialization TEXT NOT NULL
);
--rollback DROP TABLE instructor;

--changeset dirijable:3
CREATE TABLE course
(
    id            BIGSERIAL PRIMARY KEY,
    name          TEXT                    NOT NULL,
    description   TEXT                    NOT NULL,
    instructor_id BIGINT                          REFERENCES instructor (id) ON DELETE SET NULL,
    category_id   BIGINT REFERENCES category (id) NOT NULL
);
--rollback DROP TABLE course;

--changeset dirijable:4
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      TEXT UNIQUE NOT NULL,
    password   TEXT               NOT NULL,
    first_name TEXT        NOT NULL,
    last_name  TEXT        NOT NULL,
    role       TEXT        NOT NULL
);
--rollback DROP TABLE users;

--changeset dirijable:5
CREATE TABLE course_users
(
    course_id BIGINT REFERENCES course (id),
    user_id   BIGINT REFERENCES users (id),
    PRIMARY KEY (course_id, user_id)
);
--rollback DROP TABLE course_users;

--changeset dirijable:6
CREATE TABLE lesson
(
    id               BIGSERIAL PRIMARY KEY,
    title            TEXT                                            NOT NULL,
    content          TEXT                                            NOT NULL,
    duration_minutes INT                                             NOT NULL CHECK (duration_minutes > 0),
    course_id        BIGINT REFERENCES course (id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT unique_title UNIQUE (course_id, title)
);
--rollback DROP TABLE lesson;

--changeset dirijable:7
CREATE TABLE refresh_token
(
    id              BIGSERIAL PRIMARY KEY,
    expiration_date TIMESTAMPTZ                          NOT NULL,
    token           TEXT                         NOT NULL,
    user_email      TEXT REFERENCES users (email) NOT NULL
);
--rollback DROP TABLE refresh_token;