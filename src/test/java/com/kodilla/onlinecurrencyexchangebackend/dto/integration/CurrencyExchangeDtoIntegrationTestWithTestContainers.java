//package com.kodilla.onlinecurrencyexchangebackend.dto.integration;
//
//import com.kodilla.onlinecurrencyexchangebackend.dto.CurrencyExchangeDto;
//import com.kodilla.onlinecurrencyexchangebackend.nbp.config.DBConfig;
//import com.kodilla.onlinecurrencyexchangebackend.service.nbp.NBPApiService;
//import org.flywaydb.test.annotation.FlywayTest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import javax.transaction.Transactional;
//
//@Testcontainers
//@SpringBootTest
//@Transactional
//@FlywayTest
//class CurrencyExchangeDtoIntegrationTestWithTestContainers {
//
//    @Autowired
//    private NBPApiService nbpApiService;
//    @Autowired
//    private CurrencyExchangeDto currencyExchangeDto;
//    @Autowired
//    private DBConfig config;
//
//    @Container
//    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
//            .withDatabaseName("currency_exchange_project")
//            .withUsername("oce_admin")
//            .withPassword("oce_password")
//            .withInitScript("V1__initialize_database.sql");
//
//    @DynamicPropertySource
//    static void databaseProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", () -> "jdbc:mysql://localhost:" + mySQLContainer.getMappedPort(3306) +
//                "/currency_exchange_project?useSSL=false&allowPublicKeyRetrieval=true");
//        registry.add("spring.datasource.username", () -> "oce_admin");
//        registry.add("spring.datasource.password", () -> "oce_password");
//    }
//
//
////    @Test
////    void shouldGetExchangeRatesFromDatabaseTest() throws SQLException {
////        // When
////        nbpApiService.updateCurrencyRatesWithDate(LocalDate.of(2024, 4, 5));
////        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeDto.getExchangeRatesFromDatabase();
////
////        // Then
////        currencyExchangeList.forEach(System.out::println);
////        assertNotNull(currencyExchangeList);
////        assertEquals(13, currencyExchangeList.size());
////        assertFalse(currencyExchangeList.isEmpty());
////
////    }
////
////    @Test
////    void shouldGetExchangeRatesByCodeAndDateFromDatabaseTest() throws SQLException {
////        // When
////        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeDto.getExchangeRatesByCodeAndDate("USD", LocalDate.of(2024, 01, 25));
////
////        // Then
////        assertNotNull(currencyExchangeList);
////        assertFalse(currencyExchangeList.isEmpty());
////        assertEquals(1, currencyExchangeList.size());
////        currencyExchangeList.forEach(System.out::println);
////
////    }
////
////    @Test
////    void testGetExchangeRatesByCodeFromDateToDate() {
////        // Given
////        String currencyCode = "EUR";
////        LocalDate startingDate = LocalDate.of(2024, 1, 1);
////        LocalDate endingDate = LocalDate.of(2024, 1, 31);
////
////        // When
////        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeDto.getExchangeRatesByCodeFromDateToDate(currencyCode, startingDate, endingDate);
////
////        // Then
////        assertNotNull(currencyExchangeList);
////        assertFalse(currencyExchangeList.isEmpty());
////        assertEquals(2, currencyExchangeList.size());
////        currencyExchangeList.forEach(System.out::println);
////    }
////
////    @Test
////    void testGetExchangeRatesByDate() {
////        // Given
////        LocalDate effectiveDate = LocalDate.of(2024, 1, 23);
////
////        // When
////        List<CurrencyExchangeDto> currencyExchangeList = currencyExchangeDto.getExchangeRatesByDate(effectiveDate);
////
////        // Then
////        assertNotNull(currencyExchangeList);
////        assertFalse(currencyExchangeList.isEmpty());
////        assertEquals(13, currencyExchangeList.size());
////        currencyExchangeList.forEach(System.out::println);
////    }
//}