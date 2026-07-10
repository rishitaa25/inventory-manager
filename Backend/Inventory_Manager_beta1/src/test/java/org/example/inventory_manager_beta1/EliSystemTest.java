package org.example.inventory_manager_beta1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=password",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "spring.jpa.show-sql=false",
                "jwt.secret=inventory-manager-secret-key-for-sessions",
                "jwt.expiration-ms=86400000"
        }
)
public class EliSystemTest {

    @LocalServerPort
    int port;

    private static boolean setupDone = false;

    private static final String ADMIN_USER  = "eli_system_admin";
    private static final String ADMIN_PASS  = "TestPass1!";
    private static final String ADMIN_SSN   = "123-45-9999";
    private static final String ADMIN_EMAIL = "eli.system@test.com";
    private static final String ADMIN_PHONE = "555-9999";

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

        // Only insert the first admin once across all tests
        if (!setupDone) {
            String body = "{"
                    + "\"ssn\":\"" + ADMIN_SSN + "\","
                    + "\"firstName\":\"Eli\","
                    + "\"lastName\":\"Admin\","
                    + "\"userName\":\"" + ADMIN_USER + "\","
                    + "\"email\":\"" + ADMIN_EMAIL + "\","
                    + "\"phoneNumber\":\"" + ADMIN_PHONE + "\","
                    + "\"password\":\"" + ADMIN_PASS + "\","
                    + "\"managementTitle\":\"SHIFT_MANAGER\""
                    + "}";

            RestAssured.given()
                    .header("Content-Type", "application/json")
                    .body(body)
                    .when()
                    .post("/admin/signup");

            setupDone = true;
        }
    }

    // Logs in as the test admin and returns the JWT
    private String getToken() throws JSONException {
        String loginBody = "{"
                + "\"userName\":\"" + ADMIN_USER + "\","
                + "\"password\":\"" + ADMIN_PASS + "\""
                + "}";

        Response r = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post("/admin/login");

        return new JSONObject(r.getBody().asString()).optString("token", null);
    }

    @Test
    public void testAdminLogin_WrongPassword_ReturnsRejectionAndNoToken() throws JSONException {
        String body = "{"
                + "\"userName\":\"" + ADMIN_USER + "\","
                + "\"password\":\"DEFINITELYWRONGPASSWORD\""
                + "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/admin/login");

        assertEquals(200, response.getStatusCode());

        JSONObject json = new JSONObject(response.getBody().asString());
        String message = json.optString("message", "");

        assertTrue(
                message.equals("Invalid password") || message.equals("User not found"),
                "Wrong password must produce a rejection message, got: " + message
        );
        assertTrue(
                json.isNull("token") || json.optString("token", "").isEmpty(),
                "No JWT token should be issued on a failed login"
        );
    }

    @Test
    public void testAdminLogin_NonExistentUser_ReturnsUserNotFound() throws JSONException {
        String body = "{"
                + "\"userName\":\"totally_fake_user_zzz9999\","
                + "\"password\":\"somepassword\""
                + "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/admin/login");

        assertEquals(200, response.getStatusCode());

        JSONObject json = new JSONObject(response.getBody().asString());
        String message = json.optString("message", "");

        assertEquals("User not found", message,
                "Login with non-existent username must return 'User not found'");
        assertTrue(
                json.isNull("token") || json.optString("token", "").isEmpty(),
                "No token should be issued for a non-existent user"
        );
    }

    @Test
    public void testAdminSignup_SecondAdmin_AssignsAdminAccessLevel() throws JSONException {
        long ts = System.currentTimeMillis();
        String uniqueUser  = "second_" + ts;
        String uniqueSSN   = String.format("%03d-%02d-%04d",
                ts % 1000, (ts / 1000) % 100, (ts / 100000) % 10000);
        String uniqueEmail = uniqueUser + "@test.com";
        String uniquePhone = "444-" + (ts % 9000 + 1000);

        String body = "{"
                + "\"ssn\":\"" + uniqueSSN + "\","
                + "\"firstName\":\"Second\","
                + "\"lastName\":\"Admin\","
                + "\"userName\":\"" + uniqueUser + "\","
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"phoneNumber\":\"" + uniquePhone + "\","
                + "\"password\":\"TestPass1!\","
                + "\"managementTitle\":\"SHIFT_MANAGER\""
                + "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/admin/signup");

        assertEquals(200, response.getStatusCode());

        JSONObject json = new JSONObject(response.getBody().asString());
        assertEquals("ADMIN", json.optString("accessLevel", ""),
                "Second admin must receive ADMIN access level, not GENERAL_MANAGER");
    }

    @Test
    public void testGetAllItems_WithValidToken_Returns200() throws JSONException {
        String token = getToken();
        assertNotNull(token, "Login must succeed and return a token");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/item/find/all");

        assertEquals(200, response.getStatusCode(),
                "GET /item/find/all with valid token must return HTTP 200");
    }

    @Test
    public void testEmployeeSignupAndLogin_ReturnsTokenOnSuccess() throws JSONException {
        long ts = System.currentTimeMillis();
        String uniqueUser  = "emp_" + ts;
        String uniqueSSN   = String.format("%03d-%02d-%04d",
                (ts + 1) % 1000, (ts / 1000 + 1) % 100, (ts / 100000 + 1) % 10000);
        String uniqueEmail = uniqueUser + "@test.com";
        String uniquePhone = "333-" + (ts % 9000 + 1000);

        String signupBody = "{"
                + "\"ssn\":\"" + uniqueSSN + "\","
                + "\"firstName\":\"TestEmp\","
                + "\"lastName\":\"User\","
                + "\"userName\":\"" + uniqueUser + "\","
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"phoneNumber\":\"" + uniquePhone + "\","
                + "\"password\":\"EmpPass1!\""
                + "}";

        Response signupResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(signupBody)
                .when()
                .post("/employee/signup");

        assertEquals(200, signupResponse.getStatusCode());

        String loginBody = "{"
                + "\"userName\":\"" + uniqueUser + "\","
                + "\"password\":\"EmpPass1!\""
                + "}";

        Response loginResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post("/employee/login");

        assertEquals(200, loginResponse.getStatusCode());
        JSONObject json = new JSONObject(loginResponse.getBody().asString());
        assertEquals("Login successful", json.optString("message", ""));
        assertNotNull(json.optString("token", null),
                "Employee login must return a JWT token");
    }

    @Test
    public void testAdminLogout_WithValidToken_ReturnsSuccessMessage() throws JSONException {
        String token = getToken();
        assertNotNull(token, "Must have a valid token to test logout");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/admin/logout");

        assertEquals(200, response.getStatusCode());
        assertTrue(
                response.getBody().asString().contains("Logged out successfully"),
                "Logout must return a success message"
        );
    }

    @Test
    public void testCreateShift_ForEmployee_ReturnsConfirmationMessage() throws JSONException {
        String token = getToken();
        assertNotNull(token, "Must have a valid token to create a shift");

        long ts = System.currentTimeMillis();
        String uniqueUser  = "shift_emp_" + ts;
        String uniqueSSN   = String.format("%03d-%02d-%04d",
                (ts + 2) % 1000, (ts / 1000 + 2) % 100, (ts / 100000 + 2) % 10000);
        String uniqueEmail = uniqueUser + "@test.com";
        String uniquePhone = "222-" + (ts % 9000 + 1000);

        String signupBody = "{"
                + "\"ssn\":\"" + uniqueSSN + "\","
                + "\"firstName\":\"ShiftEmp\","
                + "\"lastName\":\"Test\","
                + "\"userName\":\"" + uniqueUser + "\","
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"phoneNumber\":\"" + uniquePhone + "\","
                + "\"password\":\"ShiftPass1!\""
                + "}";

        Response signupResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(signupBody)
                .when()
                .post("/employee/signup");

        assertEquals(200, signupResponse.getStatusCode());

        JSONObject signupJson = new JSONObject(signupResponse.getBody().asString());
        int employeeId = signupJson.getInt("employeeId");

        String shiftBody = "{"
                + "\"employeeId\":" + employeeId + ","
                + "\"shiftDate\":\"2025-12-01\","
                + "\"startTime\":\"09:00:00\","
                + "\"endTime\":\"17:00:00\""
                + "}";

        Response shiftResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(shiftBody)
                .when()
                .post("/shift/create");

        assertEquals(200, shiftResponse.getStatusCode());
        assertTrue(
                shiftResponse.getBody().asString().contains("has been assigned a shift"),
                "Shift creation must return a confirmation message"
        );
    }

    @Test
    public void testGetAllAdmins_WithValidToken_Returns200() throws JSONException {
        String token = getToken();
        assertNotNull(token, "Must have a valid token to list admins");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/admin/find/all");

        assertEquals(200, response.getStatusCode(),
                "GET /admin/find/all with valid token must return HTTP 200");
    }

    @Test
    public void testDriverSignupAndLogin_ReturnsTokenOnSuccess() throws JSONException {
        long ts = System.currentTimeMillis();
        String uniqueUser = "driver_" + ts;
        String uniqueSSN = String.format("%03d-%02d-%04d",
                (ts + 3) % 1000, (ts / 1000 + 3) % 100, (ts / 100000 + 3) % 10000);
        String uniqueEmail = uniqueUser + "@test.com";
        String uniquePhone = "777-" + (ts % 9000 + 1000);

        String signupBody = "{"
                + "\"ssn\":\"" + uniqueSSN + "\","
                + "\"firstName\":\"TestDriver\","
                + "\"lastName\":\"User\","
                + "\"userName\":\"" + uniqueUser + "\","
                + "\"email\":\"" + uniqueEmail + "\","
                + "\"phoneNumber\":\"" + uniquePhone + "\","
                + "\"password\":\"DriverPass1!\""
                + "}";

        Response signupResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(signupBody)
                .when()
                .post("/driver/signup");

        assertEquals(200, signupResponse.getStatusCode());

        String loginBody = "{"
                + "\"userName\":\"" + uniqueUser + "\","
                + "\"password\":\"DriverPass1!\""
                + "}";

        Response loginResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post("/driver/login");

        assertEquals(200, loginResponse.getStatusCode());
        JSONObject json = new JSONObject(loginResponse.getBody().asString());
        assertEquals("Login successful", json.optString("message", ""));
        assertNotNull(json.optString("token", null),
                "Driver login must return a JWT token");
    }

    @Test
    public void testAddShippingCompany_ThenGetAll_Returns200() throws JSONException {
        String token = getToken();
        assertNotNull(token, "Must have a valid token");

        long ts = System.currentTimeMillis();
        String body = "{"
                + "\"shippingCompanyName\":\"TestCo_" + ts + "\","
                + "\"shippingCompanyEmail\":\"testco_" + ts + "@ship.com\","
                + "\"shippingCompanyPhone\":\"888-" + (ts % 9000 + 1000) + "\""
                + "}";

        Response addResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post("/shipping-company/add");

        assertEquals(200, addResponse.getStatusCode());

        Response getResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/shipping-company/find/all");

        assertEquals(200, getResponse.getStatusCode());
        assertTrue(getResponse.getBody().asString().length() > 2,
                "Shipping company list must not be empty");
    }

    @Test
    public void testGetAllShifts_WithValidToken_Returns200() throws JSONException {
        String token = getToken();
        assertNotNull(token, "Must have a valid token to list shifts");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/shift/find/all");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testGetInventory_WithValidToken_Returns200() throws JSONException {
        String token = getToken();
        assertNotNull(token, "Must have a valid token to get inventory");

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/inventory");

        assertEquals(200, response.getStatusCode());
    }
}