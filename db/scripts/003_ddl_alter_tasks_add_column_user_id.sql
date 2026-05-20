--liquibase formatted sql

--changeset devvk:003_ddl_alter_tasks_add_column_user_id
ALTER TABLE tasks
    ADD COLUMN user_id INT REFERENCES users (id);

--rollback ALTER TABLE tasks
--rollback DROP COLUMN user_id;
