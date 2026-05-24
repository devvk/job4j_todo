--liquibase formatted SQL

--changeset devvk:006_dml_insert_categories
INSERT INTO categories (name)
VALUES ('Работа');
INSERT INTO categories (name)
VALUES ('Учёба');
INSERT INTO categories (name)
VALUES ('Дом');
INSERT INTO categories (name)
VALUES ('Здоровье');
INSERT INTO categories (name)
VALUES ('Финансы');
INSERT INTO categories (name)
VALUES ('Покупки');

--rollback TRUNCATE TABLE categories;
