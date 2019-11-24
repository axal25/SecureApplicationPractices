-- DROP DATABASE IF EXISTS demodb; -- it's in application.yml spring.flyway.init-sqls:
-- CREATE DATABASE demodb;

DROP SCHEMA IF EXISTS demos CASCADE;
CREATE SCHEMA IF NOT EXISTS demos;

DROP SCHEMA IF EXISTS safe CASCADE;
CREATE SCHEMA IF NOT EXISTS safe;

DROP SCHEMA IF EXISTS unsafe CASCADE;
CREATE SCHEMA IF NOT EXISTS unsafe;

SET search_path TO "$user$", public, safe, demos;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA demos;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA safe;

DROP TABLE IF EXISTS safe.courses;
DROP TABLE IF EXISTS unsafe.courses;

CREATE TABLE safe.courses (
     id UUID NOT NULL PRIMARY KEY,
     name VARCHAR (100) NOT NULL
);

CREATE TABLE unsafe.courses (
    id VARCHAR(100),
    name VARCHAR (100)
);

INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.1');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.2');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.3');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.4');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.5');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.6');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.7');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.8');

INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.1');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.2');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.3');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.4');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.5');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.6');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.7');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.8');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.9');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.10');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.11');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.12');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(safe.uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.13');