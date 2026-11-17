-- ============================================================
--  TESTDB Schema Reference (Microsoft SQL Server)
--  Run once as a DB admin after TESTDB exists.
--  App also auto-creates tables (spring.jpa.hibernate.ddl-auto=update).
-- ============================================================

IF DB_ID('TESTDB') IS NULL
BEGIN
    CREATE DATABASE TESTDB;
END
GO

USE TESTDB;
GO

IF OBJECT_ID('dbo.clients', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.clients (
        id          BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        name        NVARCHAR(150) NOT NULL,
        email       NVARCHAR(200) NOT NULL,
        phone       NVARCHAR(30)  NULL,
        company     NVARCHAR(150) NULL,
        city        NVARCHAR(100) NULL,
        created_at  DATETIME2     NOT NULL,
        updated_at  DATETIME2     NULL,
        CONSTRAINT uq_clients_email UNIQUE (email)
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.clients)
BEGIN
    INSERT INTO dbo.clients (name, email, phone, company, city, created_at, updated_at)
    VALUES
        ('Alice Tan',     'alice.tan@example.com',     '+60123-111111', 'Acme Sdn Bhd',    'Kuala Lumpur', SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Bob Lim',       'bob.lim@example.com',       '+60123-222222', 'Globex Tech',     'Penang',       SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Carol Ong',     'carol.ong@example.com',     '+60123-333333', 'Initech',         'Johor Bahru',  SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('David Raj',     'david.raj@example.com',     '+60123-444444', 'Umbrella Corp',   'Ipoh',         SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Eve Ng',        'eve.ng@example.com',        '+60123-555555', 'Stark Industries','Shah Alam',    SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Frank Lee',     'frank.lee@example.com',     '+60123-666666', 'Wayne Enterprises','Kuching',     SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Grace Foo',     'grace.foo@example.com',     '+60123-777777', 'Hooli',           'Kota Kinabalu',SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Henry Goh',     'henry.goh@example.com',     '+60123-888888', 'Vandelay',        'Melaka',       SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Ivy Chua',      'ivy.chua@example.com',      '+60123-999999', 'Pied Piper',      'Seremban',     SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Jason Wong',    'jason.wong@example.com',    '+60123-101010', 'Cyberdyne',       'Alor Setar',   SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Kelly Sim',     'kelly.sim@example.com',     '+60123-112233', 'Soylent',         'Kuantan',      SYSUTCDATETIME(), SYSUTCDATETIME()),
        ('Leo Chan',      'leo.chan@example.com',      '+60123-445566', 'Aperture',        'Sandakan',     SYSUTCDATETIME(), SYSUTCDATETIME());
END
GO
