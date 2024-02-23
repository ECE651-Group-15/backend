package infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CustomerProfileResourcesIT {
	private final String VALID_CUSTOMER_PROFILE_TEMPLATE = """
			{
			  "name": "Nikola Tesla",
			  "email": "nikola.tesla@example.com",
			  "password": "%s",
			  "phone": "1234567890"
			}
			""";

	private void deleteCustomerProfile(String customerId) {
		RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/profile/delete-profile/" + customerId)
				   .then()
				   .statusCode(200);
	}

	@Test
	public void createCustomerProfile_whenAllInfoProvided_createsProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");
		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla"))
									   .extract().path("data.id");

		deleteCustomerProfile(customerId);
	}

	@Test
	public void createCustomerProfile_whenPasswordIsEmpty_doNotCreateProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "");
		RestAssured.given()
				   .contentType("application/json")
				   .body(validProfile)
				   .when().post("/v1/api/profile/create-profile")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001));
	}

	@Test
	public void createCustomerProfile_whenEmailExists_doNotCreateProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");
		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when().post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla"))
									   .extract().path("data.id");

		RestAssured.given()
				   .contentType("application/json")
				   .body(validProfile)
				   .when().post("/v1/api/profile/create-profile")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001));

		deleteCustomerProfile(customerId);
	}

	@Test
	public void getCustomerProfile_whenCustomerExists_returnsProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");
		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla"))
									   .extract().path("data.id");

		RestAssured.given()
				   .contentType("application/json")
				   .body(validProfile)
				   .when()
				   .post("/v1/api/profile/get-profile/" + customerId)
				   .then()
				   .statusCode(200)
				   .body("data.name", is("Nikola Tesla"))
				   .body("data.id", is(customerId));

		deleteCustomerProfile(customerId);
	}

	@Test
	public void getCustomerProfile_whenCustomerNotExist_doNotReturnProfile() {
		RestAssured.given()
				   .contentType("application/json")
				   .when()
				   .post("/v1/api/profile/get-profile/" + "invalid_customer_id")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Cannot find customer with id"));
	}
}
