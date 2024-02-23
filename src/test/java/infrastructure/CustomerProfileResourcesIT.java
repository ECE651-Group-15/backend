package infrastructure;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

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
}
