--liquibase formatted SQL

--changeset devvk:005_ddl_create_categories_table
CREATE TABLE categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

--rollback DROP TABLE categories;
