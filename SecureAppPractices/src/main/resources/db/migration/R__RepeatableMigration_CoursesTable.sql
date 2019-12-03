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
                REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA safe FROM limitedsafe;
                REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA safe FROM limitedsafe;
                REVOKE ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA safe FROM limitedsafe;
                REVOKE ALL PRIVILEGES ON SCHEMA safe FROM limitedsafe;
                REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public FROM limitedsafe;
                REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM limitedsafe;
                REVOKE ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public FROM limitedsafe;
                REVOKE ALL PRIVILEGES ON SCHEMA public FROM limitedsafe;
                DROP TABLE IF EXISTS safe.courses CASCADE;
                DROP SCHEMA IF EXISTS safe CASCADE;
            END IF;
        END IF;
    END;
$do$;

DO
$do$
    BEGIN
        IF EXISTS(
                SELECT
                FROM pg_catalog.pg_namespace
                WHERE nspname = 'safe'
            ) THEN
            DROP SCHEMA IF EXISTS safe CASCADE;
        END IF;
    END;
$do$;

DROP SCHEMA IF EXISTS safe CASCADE;

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
                SELECT
                FROM pg_catalog.pg_namespace
                WHERE nspname = 'unsafe'
           ) THEN
           IF EXISTS(
               SELECT
               FROM information_schema.tables
               WHERE(
                    table_schema = 'unsafe'
                    AND
                    table_name = 'courses'
               )
           ) THEN
               ALTER TABLE unsafe.courses OWNER TO postgres;
           END IF;
       END IF;
    END;
$do$;


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
                REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA unsafe FROM limitedunsafe;
                REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA unsafe FROM limitedunsafe;
                REVOKE ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA unsafe FROM limitedunsafe;
                REVOKE ALL PRIVILEGES ON SCHEMA unsafe FROM limitedunsafe;
                REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public FROM limitedunsafe;
                REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM limitedunsafe;
                REVOKE ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public FROM limitedunsafe;
                REVOKE ALL PRIVILEGES ON SCHEMA public FROM limitedunsafe;
                DROP TABLE IF EXISTS unsafe.courses CASCADE;
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

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER EXTENSION "uuid-ossp" SET SCHEMA public;

SET search_path TO "$user$", public, safe, unsafe;

CREATE TABLE safe.courses (
     id UUID DEFAULT uuid_generate_v4() NOT NULL PRIMARY KEY,
     name VARCHAR (100) NOT NULL
);

INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.1');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.2');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.3');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.4');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.5');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.6');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.7');
INSERT INTO safe.courses (id, name) VALUES (uuid_generate_v4(), 'Predefined Example Course from /resources/db/migration/*.sql file #1.8');

CREATE TABLE unsafe.courses (
    id VARCHAR(100),
    name VARCHAR (100)
);

INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.1');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.2');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.3');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.4');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.5');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.6');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.7');
INSERT INTO unsafe.courses (id, name) VALUES ( CAST(uuid_generate_v4() AS VARCHAR ), 'Predefined Example Course from /resources/db/migration/*.sql file #2.8');

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
            GRANT USAGE ON SCHEMA public TO limitedsafe;
            GRANT USAGE ON SCHEMA safe TO limitedsafe;
--             GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA safe TO limitedsafe;
            GRANT EXECUTE ON FUNCTION uuid_generate_v4() TO limitedsafe;
            GRANT SELECT, UPDATE, INSERT, DELETE ON safe.courses TO limitedsafe;
        END IF;
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
            GRANT USAGE ON SCHEMA public TO limitedunsafe;
            GRANT USAGE ON SCHEMA unsafe TO limitedunsafe;
            GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA unsafe TO limitedunsafe;
            GRANT ALL PRIVILEGES ON TABLE unsafe.courses TO limitedunsafe;
            ALTER TABLE unsafe.courses OWNER TO limitedunsafe;
        end if;
    END;
$do$;

-- SELECT grantee, privilege_type
-- FROM information_schema.role_table_grants
-- WHERE table_name='courses';




