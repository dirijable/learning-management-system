--changeset dirijable:1
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

ALTER TABLE users
    ALTER COLUMN role TYPE user_role
        USING role::user_role;

--rollback ALTER TABLE users ALTER COLUMN role TYPE TEXT;
--rollback DROP TYPE user_role;