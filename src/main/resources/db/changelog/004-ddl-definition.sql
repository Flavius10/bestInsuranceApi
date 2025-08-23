-- liquibase formatted sql
-- changeset Flavius:004-ddl-definition.sql splitStatements:false

ALTER TABLE policies
ALTER COLUMN price TYPE NUMERIC(8,2);
