--liquibase formatted sql

--changeset devvk:001_ddl_create_tasks_table
CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    title   VARCHAR(255) NOT NULL,
    description TEXT,
    created TIMESTAMP    NOT NULL,
    done        BOOLEAN
);

--rollback DROP TABLE tasks;
