--changeset dirijable:1
ALTER TABLE category RENAME TO categories;
ALTER TABLE instructor RENAME TO instructors;
ALTER TABLE course RENAME TO courses;
ALTER TABLE lesson RENAME TO lessons;
ALTER TABLE refresh_token RENAME TO refresh_tokens;

ALTER TABLE course_users RENAME TO courses_users;

--rollback ALTER TABLE categories RENAME TO category;
--rollback ALTER TABLE instructors RENAME TO instructor;
--rollback ALTER TABLE courses RENAME TO course;
--rollback ALTER TABLE lessons RENAME TO lesson;
--rollback ALTER TABLE refresh_tokens RENAME TO refresh_token;
--rollback ALTER TABLE courses_users RENAME TO course_users;
