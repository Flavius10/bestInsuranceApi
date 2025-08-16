--liquibase formatted sql
--changeset Flavius:001-ddl-definition.sql splitStatements:false

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Countries(

    country_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(64) NOT NULL,
    population INT

);

CREATE TABLE States(

    state_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(64) NOT NULL,
    population INT,
    country_id UUID NOT NULL,
    CONSTRAINT states_countries_fk FOREIGN KEY (country_id) REFERENCES Countries (country_id)

);

CREATE TABLE Cities(

    city_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL,
    state_id UUID,
    name VARCHAR(64) NOT NULL,
    population INT,
    CONSTRAINT cities_countries_fk FOREIGN KEY (country_id) REFERENCES Countries (country_id),
    CONSTRAINT cities_states_fk FOREIGN KEY (state_id) REFERENCES States (state_id)

);

CREATE TABLE Addresses(

    address_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    country_id UUID NOT NULL,
    city_id UUID NOT NULL,
    state_id UUID,
    address VARCHAR(128) NOT NULL,
    postal_code VARCHAR(16),

    CONSTRAINT addresses_countries_fk FOREIGN KEY (country_id) REFERENCES Countries (country_id),
    CONSTRAINT addressed_cities_fk FOREIGN KEY (city_id) REFERENCES Cities (city_id),
    CONSTRAINT addresses_states_fk FOREIGN KEY (state_id) REFERENCES States (state_id)

);

CREATE TABLE Customers(

    customer_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(64) NOT NULL,
    surname VARCHAR(64) NOT NULL,
    email VARCHAR(320) NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    updated TIMESTAMP WITH TIME ZONE NOT NULL,
    address_id UUID,

    CONSTRAINT customers_addresses_fk FOREIGN KEY (address_id) REFERENCES Addresses (address_id)

);

CREATE TABLE Coverages(

    coverage_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(64) NOT NULL,
    description TEXT

);

CREATE TABLE Policies(

    policy_id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(16) NOT NULL,
    description TEXT,
    price NUMERIC(4,2) NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    updated TIMESTAMP WITH TIME ZONE NOT NULL

);

CREATE TABLE Policies_Coverages(

    coverage_id UUID NOT NULL,
    policy_id UUID NOT NULL,

    CONSTRAINT pc_policies_coverages PRIMARY KEY (coverage_id, policy_id),
    CONSTRAINT pc_policies_fk FOREIGN KEY (policy_id) REFERENCES Policies (policy_id),
    CONSTRAINT pc_coverages_fk FOREIGN KEY (coverage_id) REFERENCES Coverages (coverage_id)

);

CREATE TABLE Subscriptions(

    policy_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE  NOT NULL,
    paid_price NUMERIC(6,2) NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    updated TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT subscriptions_pk PRIMARY KEY (policy_id, customer_id),
    CONSTRAINT subscriptions_policies_fk FOREIGN KEY (policy_id) REFERENCES Policies (policy_id),
    CONSTRAINT subscriptions_customers_fk FOREIGN KEY (customer_id) REFERENCES Customers (customer_id)

);