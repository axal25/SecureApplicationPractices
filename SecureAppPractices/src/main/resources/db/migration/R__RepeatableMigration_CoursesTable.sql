DROP TABLE IF EXISTS safe.courses;
DROP TABLE IF EXISTS unsafe.courses;

DROP SCHEMA IF EXISTS safe CASCADE;
DROP SCHEMA IF EXISTS unsafe CASCADE;

------------------------------------------------------------------------------------------------------------------------
--- LIMITED USER FOR SAFE ---
------------------------------------------------------------------------------------------------------------------------

DO
$do$
    BEGIN
        IF EXISTS(
                SELECT -- SELECT list can stay empty for this
                FROM pg_catalog.pg_roles
                WHERE  rolname = 'limitedsafe'
            ) THEN
            IF EXISTS(
                SELECT
                FROM pg_catalog.pg_namespace
                WHERE nspname = 'safe'
                ) THEN
                REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA safe FROM limitedsafe;
                DROP SCHEMA IF EXISTS safe CASCADE;
            END IF;
        END IF;
    END;
$do$;

DO
$do$
    BEGIN
        IF EXISTS (
                SELECT -- SELECT list can stay empty for this
                FROM   pg_catalog.pg_roles
                WHERE  rolname = 'limitedsafe'
            ) THEN
            DROP ROLE limitedsafe;
        END IF;
    END;
$do$;

------------------------------------------------------------------------------------------------------------------------
--- LIMITED USER FOR UN-SAFE ---
------------------------------------------------------------------------------------------------------------------------

DO
$do$
    BEGIN
        IF EXISTS(
                SELECT -- SELECT list can stay empty for this
                FROM pg_catalog.pg_roles
                WHERE  rolname = 'limitedunsafe'
            ) THEN
            IF EXISTS(
                    SELECT
                    FROM pg_catalog.pg_namespace
                    WHERE nspname = 'unsafe'
                ) THEN
                REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA unsafe FROM limitedunsafe;
                DROP SCHEMA IF EXISTS unsafe CASCADE;
            END IF;
        END IF;
    END;
$do$;

DO
$do$
    BEGIN
        IF EXISTS (
                SELECT -- SELECT list can stay empty for this
                FROM   pg_catalog.pg_roles
                WHERE  rolname = 'limitedunsafe'
            ) THEN
            DROP ROLE limitedunsafe;
        END IF;
    END;
$do$;

CREATE SCHEMA IF NOT EXISTS safe;
CREATE SCHEMA IF NOT EXISTS unsafe;

SET search_path TO "$user$", public, safe, unsafe;

CREATE TABLE safe.courses (
     id UUID NOT NULL PRIMARY KEY,
     name VARCHAR (100) NOT NULL
);

CREATE TABLE unsafe.courses (
    id VARCHAR(100),
    name VARCHAR (100)
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA safe;

INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.1');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.2');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.3');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.4');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.5');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.6');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.7');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.8');

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA unsafe;

INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.1');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.2');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.3');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.4');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.5');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.6');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.7');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.8');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.9');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.10');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.11');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.12');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.13');

------------------------------------------------------------------------------------------------------------------------
--- LIMITED USER FOR SAFE ---
------------------------------------------------------------------------------------------------------------------------

DO
$do$
    BEGIN
        IF NOT EXISTS (
                SELECT -- SELECT list can stay empty for this
                FROM   pg_catalog.pg_roles
                WHERE  rolname = 'limitedsafe'
            ) THEN
            CREATE ROLE limitedsafe LOGIN PASSWORD 'limitedsafe_password';
        END IF;
    END;
$do$;

-- SELECT * FROM pg_catalog.pg_roles WHERE rolname = 'limitedsafe';
-- SELECT * FROM pg_catalog.pg_user WHERE usename = 'limitedsafe';

-- SELECT d.datname,
--        (SELECT string_agg(u.usename, ',' order by u.usename)
--         FROM pg_user u
--         WHERE has_database_privilege(u.usename, d.datname, 'CONNECT')) AS allowed_users
-- FROM pg_database d
-- ORDER BY d.datname;

DO
$do$
    BEGIN
        IF EXISTS(
                SELECT -- SELECT list can stay empty for this
                FROM   pg_catalog.pg_roles
                WHERE  rolname = 'limitedsafe'
            ) THEN
            GRANT USAGE ON SCHEMA safe TO limitedsafe;
            GRANT SELECT, UPDATE, INSERT, DELETE ON safe.courses TO limitedsafe;
        end if;
    END;
$do$;

-- SELECT grantee, privilege_type
-- FROM information_schema.role_table_grants
-- WHERE table_name='courses';

------------------------------------------------------------------------------------------------------------------------
--- LIMITED USER FOR UN-SAFE ---
------------------------------------------------------------------------------------------------------------------------

DO
$do$
    BEGIN
        IF NOT EXISTS (
                SELECT -- SELECT list can stay empty for this
                FROM   pg_catalog.pg_roles
                WHERE  rolname = 'limitedunsafe'
            ) THEN
            CREATE ROLE limitedunsafe LOGIN PASSWORD 'limitedunsafe_password';
        END IF;
    END;
$do$;

-- SELECT * FROM pg_catalog.pg_roles WHERE rolname = 'limitedunsafe';
-- SELECT * FROM pg_catalog.pg_user WHERE usename = 'limitedunsafe';

-- SELECT d.datname,
--        (SELECT string_agg(u.usename, ',' order by u.usename)
--         FROM pg_user u
--         WHERE has_database_privilege(u.usename, d.datname, 'CONNECT')) AS allowed_users
-- FROM pg_database d
-- ORDER BY d.datname;

DO
$do$
    BEGIN
        IF EXISTS(
                SELECT -- SELECT list can stay empty for this
                FROM   pg_catalog.pg_roles
                WHERE  rolname = 'limitedunsafe'
            ) THEN
            GRANT USAGE ON SCHEMA safe TO limitedunsafe;
            GRANT SELECT, UPDATE, INSERT, DELETE ON unsafe.courses TO limitedunsafe;
        end if;
    END;
$do$;

-- SELECT grantee, privilege_type
-- FROM information_schema.role_table_grants
-- WHERE table_name='courses';




