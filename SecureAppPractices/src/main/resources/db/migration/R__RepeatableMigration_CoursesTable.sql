-- DROP DATABASE IF EXISTS demodb; -- it's in application.yml spring.flyway.init-sqls:
-- CREATE DATABASE demodb;

DROP SCHEMA IF EXISTS demos CASCADE;
CREATE SCHEMA IF NOT EXISTS demos;

DROP SCHEMA IF EXISTS safe CASCADE;
CREATE SCHEMA IF NOT EXISTS safe;

SET search_path TO "$user$", public, safe, demos;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA demos;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA safe;

DROP TABLE IF EXISTS safe.courses;

CREATE TABLE safe.courses (
     id UUID NOT NULL PRIMARY KEY,
     name VARCHAR (100) NOT NULL
);

INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #2');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #3');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #4');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #5');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #6');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #7');
INSERT INTO safe.courses (id, name) VALUES (safe.uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #8');