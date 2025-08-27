-- liquibase formatted sql
-- changeset Flavius:005-ddl-definition.sql splitStatements:false

ALTER TABLE CUSTOMERS ADD COLUMN birth_date DATE NOT NULL;