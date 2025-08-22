--liquibase formatted sql
--changeset Flavius:003-ddl-definition.sql splitStatements:false

-- Countries
INSERT INTO countries (country_id, name)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'United States'),
    ('22222222-2222-2222-2222-222222222222', 'Canada');

-- States
INSERT INTO states (state_id, name, country_id)
VALUES
    ('aaaaaaa1-0000-0000-0000-aaaaaaaaaaa1', 'California', '11111111-1111-1111-1111-111111111111'),
    ('aaaaaaa2-0000-0000-0000-aaaaaaaaaaa2', 'New York', '11111111-1111-1111-1111-111111111111'),
    ('aaaaaaa3-0000-0000-0000-aaaaaaaaaaa3', 'Texas', '11111111-1111-1111-1111-111111111111'),
    ('aaaaaaa4-0000-0000-0000-aaaaaaaaaaa4', 'Ontario', '22222222-2222-2222-2222-222222222222'),
    ('aaaaaaa5-0000-0000-0000-aaaaaaaaaaa5', 'Quebec', '22222222-2222-2222-2222-222222222222');

-- Cities (UUIDs are the ones used in SampleDataLoader)
INSERT INTO cities (city_id, name, state_id, country_id)
VALUES
    ('45576d7c-8d84-4422-9440-19ef80fa16f3', 'Los Angeles', 'aaaaaaa1-0000-0000-0000-aaaaaaaaaaa1', '11111111-1111-1111-1111-111111111111'),
    ('91f360d5-811b-417c-a202-f5ba4b34b895', 'New York City', 'aaaaaaa2-0000-0000-0000-aaaaaaaaaaa2', '11111111-1111-1111-1111-111111111111'),
    ('144b05b6-ebf6-43a8-836d-0998c2c20a3c', 'Houston', 'aaaaaaa3-0000-0000-0000-aaaaaaaaaaa3', '11111111-1111-1111-1111-111111111111'),
    ('74716a04-d538-4441-84bf-7c41470778ca', 'Toronto', 'aaaaaaa4-0000-0000-0000-aaaaaaaaaaa4', '22222222-2222-2222-2222-222222222222'),
    ('eb5e9505-8580-4857-9195-6bee0324ac0f', 'Montreal', 'aaaaaaa5-0000-0000-0000-aaaaaaaaaaa5', '22222222-2222-2222-2222-222222222222');
