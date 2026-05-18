--liquibase formatted sql

--changeset devvk:001_ddl_create_tasks_table
CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    description TEXT,
    created     TIMESTAMP,
    done        BOOLEAN
);

--rollback DROP TABLE tasks;
