-- Table migration

CREATE TABLE IF NOT EXISTS currencies
(
    CURRENCY_ID bigint auto_increment
        primary key,
    CODE        varchar(255) null,
    NAME        varchar(255) null
);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    EXCHANGE_RATE_ID bigint auto_increment
        primary key,
    AVERAGE_RATE     decimal(8, 4) null,
    BUYING_RATE      decimal(8, 4) null,
    EFFECTIVE_DATE   date          null,
    SELLING_RATE     decimal(8, 4) null,
    CURRENCY_ID      bigint        null,
        foreign key (CURRENCY_ID) references currencies (CURRENCY_ID)
);

CREATE TABLE IF NOT EXISTS flyway_schema_history
(
    installed_rank int                                 not null
        primary key,
    version        varchar(50)                         null,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

create index flyway_schema_history_s_idx
    on flyway_schema_history (success);

CREATE TABLE IF NOT EXISTS gold_prices
(
    GOLD_ID        bigint auto_increment
        primary key,
    EFFECTIVE_DATE date           null,
    GOLD_PRICE     decimal(19, 2) null
);

CREATE TABLE IF NOT EXISTS users
(
    USER_ID   bigint auto_increment
        primary key,
    EMAIL     varchar(255) not null,
    PASSWORD  varchar(255) not null,
    ROLE      varchar(255) null,
    USER_NAME varchar(255) not null,
        unique (USER_NAME),
        unique (EMAIL)
);

CREATE TABLE IF NOT EXISTS subscribed_users
(
    CURRENCY_ID bigint not null,
    USER_ID     bigint not null,
    primary key (CURRENCY_ID, USER_ID),
        foreign key (USER_ID) references users (USER_ID),
        foreign key (CURRENCY_ID) references currencies (CURRENCY_ID)
);

-- Procedure migration

CREATE PROCEDURE listRates()
BEGIN
    SELECT c.CODE AS CURRENCY_CODE,
           c.NAME AS CURRENCY_NAME,
           er.SELLING_RATE,
           er.BUYING_RATE,
           er.AVERAGE_RATE,
           er.EFFECTIVE_DATE
    FROM currencies c
             JOIN
         exchange_rates er ON c.currency_id = er.currency_id;
END;

CREATE PROCEDURE listRatesByDate(IN effectiveDate DATE)
BEGIN
    SELECT c.CODE AS CURRENCY_CODE,
           c.NAME AS CURRENCY_NAME,
           er.SELLING_RATE,
           er.BUYING_RATE,
           er.AVERAGE_RATE,
           er.EFFECTIVE_DATE
    FROM currencies c
             JOIN
         exchange_rates er ON c.currency_id = er.currency_id
    WHERE er.EFFECTIVE_DATE = effectiveDate;
END;

CREATE PROCEDURE listRatesByCodeAndDate(IN currencyCode VARCHAR(255), IN effectiveDate DATE)
BEGIN
    SELECT c.CODE AS CURRENCY_CODE,
           c.NAME AS CURRENCY_NAME,
           er.SELLING_RATE,
           er.BUYING_RATE,
           er.AVERAGE_RATE,
           er.EFFECTIVE_DATE
    FROM currencies c
             JOIN
         exchange_rates er ON c.currency_id = er.currency_id
    WHERE c.CODE = currencyCode
      AND er.EFFECTIVE_DATE = effectiveDate;
END;

CREATE PROCEDURE listRatesByCodeFromDateToDate(IN currencyCode VARCHAR(255), IN startingDate DATE, IN endingDate DATE)
BEGIN
    SELECT c.CODE AS CURRENCY_CODE,
           c.NAME AS CURRENCY_NAME,
           er.SELLING_RATE,
           er.BUYING_RATE,
           er.AVERAGE_RATE,
           er.EFFECTIVE_DATE
    FROM currencies c
             JOIN
         exchange_rates er ON c.currency_id = er.currency_id
    WHERE c.CODE = currencyCode
      AND er.EFFECTIVE_DATE BETWEEN startingDate AND endingDate;
END;
