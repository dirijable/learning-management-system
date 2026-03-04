--liquibase formatted sql

--changeset dirijable:8
-- Расширяем все строковые поля до TEXT
ALTER TABLE category ALTER COLUMN name TYPE TEXT;

ALTER TABLE instructor ALTER COLUMN first_name TYPE TEXT;
ALTER TABLE instructor ALTER COLUMN last_name TYPE TEXT;
ALTER TABLE instructor ALTER COLUMN description TYPE TEXT;
ALTER TABLE instructor ALTER COLUMN specialization TYPE TEXT;

ALTER TABLE course ALTER COLUMN name TYPE TEXT;
ALTER TABLE course ALTER COLUMN description TYPE TEXT;

ALTER TABLE users ALTER COLUMN first_name TYPE TEXT;
ALTER TABLE users ALTER COLUMN last_name TYPE TEXT;
-- email и role оставляем VARCHAR или тоже меняем на TEXT для единообразия
ALTER TABLE users ALTER COLUMN email TYPE TEXT;
ALTER TABLE users ALTER COLUMN role TYPE TEXT;

ALTER TABLE lesson ALTER COLUMN title TYPE TEXT;
ALTER TABLE lesson ALTER COLUMN content TYPE TEXT;

ALTER TABLE refresh_token ALTER COLUMN token TYPE TEXT;
ALTER TABLE refresh_token ALTER COLUMN user_email TYPE TEXT;

--rollback ALTER TABLE category ALTER COLUMN name TYPE VARCHAR(64);
--rollback ALTER TABLE instructor ALTER COLUMN first_name TYPE VARCHAR(64);
--rollback ALTER TABLE instructor ALTER COLUMN last_name TYPE VARCHAR(64);
--rollback ALTER TABLE instructor ALTER COLUMN description TYPE VARCHAR(512);
--rollback ALTER TABLE instructor ALTER COLUMN specialization TYPE VARCHAR(128);
--rollback ALTER TABLE course ALTER COLUMN name TYPE VARCHAR(128);
--rollback ALTER TABLE course ALTER COLUMN description TYPE VARCHAR(512);
--rollback ALTER TABLE users ALTER COLUMN first_name TYPE VARCHAR(64);
--rollback ALTER TABLE users ALTER COLUMN last_name TYPE VARCHAR(64);
--rollback ALTER TABLE users ALTER COLUMN email TYPE VARCHAR(64);
--rollback ALTER TABLE users ALTER COLUMN role TYPE VARCHAR(32);
--rollback ALTER TABLE lesson ALTER COLUMN title TYPE VARCHAR(128);
--rollback ALTER TABLE lesson ALTER COLUMN content TYPE VARCHAR(128);
--rollback ALTER TABLE refresh_token ALTER COLUMN token TYPE VARCHAR(128);
--rollback ALTER TABLE refresh_token ALTER COLUMN user_email TYPE VARCHAR(64);