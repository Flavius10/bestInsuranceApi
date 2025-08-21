--liquibase formatted sql
--changeset Flavius:002-ddl-definition.sql splitStatements:false

ALTER TABLE Customers
ADD COLUMN telephone_number VARCHAR(20);