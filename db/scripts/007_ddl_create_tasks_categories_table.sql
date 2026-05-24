--liquibase formatted SQL

--changeset devvk:007_ddl_create_tasks_categories_table
CREATE TABLE tasks_categories
(
    task_id     INT NOT NULL REFERENCES tasks (id),
    category_id INT NOT NULL REFERENCES categories (id),
    PRIMARY KEY (task_id, category_id)
);

--rollback DROP TABLE tasks_categories;
