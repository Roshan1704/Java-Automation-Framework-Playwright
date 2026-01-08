package com.enterprise.automation.util;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Test data generation using JavaFaker
 */
public class TestDataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final Faker faker = new Faker();

    private TestDataGenerator() {}

    // User data generators
    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    public static String generatePassword() {
        return faker.internet().password(10, 20, true, true);
    }

    public static String generateUsername() {
        return faker.name().username();
    }

    // Business data generators
    public static String generateCompanyName() {
        return faker.company().name();
    }

    public static String generateJobTitle() {
        return faker.job().title();
    }

    public static String generateAddress() {
        return faker.address().fullAddress();
    }

    public static String generateCity() {
        return faker.address().city();
    }

    public static String generateZipCode() {
        return faker.address().zipCode();
    }

    public static String generateCountry() {
        return faker.address().country();
    }

    // Financial data generators
    public static String generateCreditCardNumber() {
        return faker.finance().creditCard();
    }

    public static String generateCurrency() {
        return faker.currency().code();
    }

    public static String generateCvv() {
        return faker.numerify("###");
    }

    // Date/Time generators
    public static String generateFutureDate(String format) {
        LocalDate futureDate = LocalDate.now().plusDays(faker.random().nextInt(1, 365));
        return futureDate.format(DateTimeFormatter.ofPattern(format));
    }

    public static String generatePastDate(String format) {
        LocalDate pastDate = LocalDate.now().minusDays(faker.random().nextInt(1, 365));
        return pastDate.format(DateTimeFormatter.ofPattern(format));
    }

    // Random data generators
    public static String generateRandomString(int length) {
        return faker.lorem().characters(length);
    }

    public static int generateRandomNumber(int min, int max) {
        return faker.random().nextInt(min, max);
    }

    public static boolean generateRandomBoolean() {
        return faker.random().nextBoolean();
    }

    public static String generateLoremText(int wordCount) {
        return faker.lorem().words(wordCount).toString();
    }

    public static void logGeneratedData(String dataType, String value) {
        logger.debug("Generated {} : {}", dataType, value);
    }
}
