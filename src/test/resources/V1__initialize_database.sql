-- Table migration

CREATE TABLE IF NOT EXISTS currencies
(
    CURRENCY_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    CODE        VARCHAR(255) NOT NULL,
    NAME        VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    EXCHANGE_RATE_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    AVERAGE_RATE     DECIMAL(8, 4),
    BUYING_RATE      DECIMAL(8, 4),
    EFFECTIVE_DATE   DATE,
    SELLING_RATE     DECIMAL(8, 4),
    CURRENCY_ID      BIGINT,
    FOREIGN KEY (CURRENCY_ID) REFERENCES currencies (CURRENCY_ID)
);

CREATE TABLE IF NOT EXISTS gold_prices
(
    GOLD_ID        BIGINT AUTO_INCREMENT PRIMARY KEY,
    EFFECTIVE_DATE DATE,
    GOLD_PRICE     DECIMAL(19, 2)
);

CREATE TABLE IF NOT EXISTS users
(
    USER_ID    BIGINT AUTO_INCREMENT PRIMARY KEY,
    EMAIL      VARCHAR(255),
    FIRST_NAME VARCHAR(255),
    LAST_NAME  VARCHAR(255),
    ROLE       VARCHAR(255)
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
