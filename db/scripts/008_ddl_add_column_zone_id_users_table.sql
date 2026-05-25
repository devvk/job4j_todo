--liquibase formatted SQL

--changeset devvk:008_ddl_add_column_zone_id_users_table
ALTER TABLE users
    ADD COLUMN zone_id VARCHAR(100);

--rollback ALTER TABLE users DROP COLUMN zone_id;
