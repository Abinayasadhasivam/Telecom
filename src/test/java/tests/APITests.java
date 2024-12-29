package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.TokenManager;

import static io.restassured.RestAssured.given;

public class APITests {
    private static final String BASE_URL = "https://thinking-tester-contact-list.herokuapp.com";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test(priority = 1)
    public void testAddUser() {
        String payload = """
            {
                "firstName": "Test",
                "lastName": "User",
                "email": "test@fake.com",
                "password": "myPassword"
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/users");

        Assert.assertEquals(response.getStatusCode(), 201);
        String token = response.jsonPath().getString("token");
        TokenManager.setToken(token);
        Assert.assertNotNull(token, "Token should not be null");
    }

    @Test(priority = 2)
    public void testGetUserProfile() {
        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .get("/users/me");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("firstName"));
    }

    @Test(priority = 3)
    public void testUpdateUser() {
        String payload = """
            {
                "firstName": "Updated",
                "lastName": "Username",
                "email": "test2@fake.com",
                "password": "myNewPassword"
            }
            """;

        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .patch("/users/me");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testLoginUser() {
        String payload = """
            {
                "email": "test2@fake.com",
                "password": "myNewPassword"
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/users/login");

        Assert.assertEquals(response.getStatusCode(), 200);
        String token = response.jsonPath().getString("token");
        TokenManager.setToken(token);
        Assert.assertNotNull(token, "Token should not be null");
    }

    @Test(priority = 5)
    public void testAddContact() {
        String payload = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "birthdate": "1970-01-01",
                "email": "jdoe@fake.com",
                "phone": "8005555555",
                "street1": "1 Main St.",
                "street2": "Apartment A",
                "city": "Anytown",
                "stateProvince": "KS",
                "postalCode": "12345",
                "country": "USA"
            }
            """;

        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .post("/contacts");

        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 6)
    public void testGetContactList() {
        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .get("/contacts");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 7)
    public void testGetContact() {
        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .get("/contacts");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 8)
    public void testUpdateContactFull() {
        String payload = """
            {
                "firstName": "Amy",
                "lastName": "Miller",
                "birthdate": "1992-02-02",
                "email": "amiller@fake.com",
                "phone": "8005554242",
                "street1": "13 School St.",
                "street2": "Apt. 5",
                "city": "Washington",
                "stateProvince": "QC",
                "postalCode": "A1A1A1",
                "country": "Canada"
            }
            """;

        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .put("/contacts");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    public void testUpdateContactPartial() {
        String payload = """
            {
                "firstName": "Anna"
            }
            """;

        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .contentType(ContentType.JSON)
                .body(payload)
                .patch("/contacts");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 10)
    public void testLogoutUser() {
        Response response = given()
                .header("Authorization", "Bearer " + TokenManager.getToken())
                .post("/users/logout");

        Assert.assertEquals(response.getStatusCode(), 200);
    }
}
