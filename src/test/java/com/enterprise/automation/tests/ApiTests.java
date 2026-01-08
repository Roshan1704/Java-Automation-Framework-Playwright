package com.enterprise.automation.tests;

import com.enterprise.automation.config.Configuration;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * API Testing Examples with RestAssured
 * Demonstrates API + UI hybrid testing patterns
 */
@Feature("API")
public class ApiTests extends BaseTest {

    @BeforeClass
    public void setUpApi() {
        RestAssured.baseURI = Configuration.getInstance().getApiBaseUrl();
        RestAssured.basePath = "/api/v1";
    }

    @Test(description = "Verify user list API endpoint")
    @Story("API User Management")
    @Severity(SeverityLevel.MINOR)
    public void testGetUsersList() {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAuthToken())
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThan(0))
                .extract()
                .response();

        int userCount = response.jsonPath().getInt("data.size()");
        logger.info("Retrieved {} users from API", userCount);
    }

    @Test(description = "Verify create user API")
    @Story("API User Management")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUser() {
        String requestBody = "{\n" +
                "  \"name\": \"Test User\",\n" +
                "  \"email\": \"testuser@example.com\",\n" +
                "  \"password\": \"SecurePass123\"\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + getAuthToken())
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("data.id", notNullValue())
                .extract()
                .response();

        String userId = response.jsonPath().getString("data.id");
        logger.info("Created user with ID: {}", userId);
    }

    @Test(description = "Verify API error handling")
    @Story("API Error Handling")
    @Severity(SeverityLevel.MINOR)
    public void testApiErrorHandling() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer invalid_token")
                .when()
                .get("/users")
                .then()
                .statusCode(401)
                .body("error", notNullValue());

        logger.info("Error handling verified");
    }

    /**
     * Get auth token for API requests
     */
    private String getAuthToken() {
        String requestBody = "{\n" +
                "  \"email\": \"api@example.com\",\n" +
                "  \"password\": \"ApiPass123\"\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        return response.jsonPath().getString("data.token");
    }
}
