--liquibase formatted SQL

--changeset devvk:004_ddl_create_priorities_table
CREATE TABLE priorities
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL UNIQUE,
    position INT
);

INSERT INTO priorities (name, position)
VALUES ('urgently', 1);
INSERT INTO priorities (name, position)
VALUES ('normal', 2);

ALTER TABLE tasks
    ADD COLUMN priority_id INT REFERENCES priorities (id);

UPDATE tasks
SET priority_id = (SELECT id FROM priorities WHERE name = 'urgently');

--rollback ALTER TABLE tasks DROP COLUMN priority_id;
--rollback DROP TABLE priorities;
