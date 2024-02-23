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

	private final String UPDATE_CUSTOMER_PROFILE_TEMPLATE = """
			{
			    "id": "%s",
			    "name": "John Does",
			    "email": "%s",
			    "password": "%s",
			    "avatar": "https://gravatar.com/avatar/8eb1b522f60d11fa897de1dc6351b7e8?s=400&d=robohash&r=",
			    "phone": "1122335544",
			    "longitude": 12,
			    "latitude": 45
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

	@Test
	public void updateCustomerProfile_whenCustomerExists_updatesProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when().post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla"))
									   .extract().path("data.id");

		String validUpdateProfile = String.format(UPDATE_CUSTOMER_PROFILE_TEMPLATE,
												  customerId,
												  customerId,
												  "123456");

		RestAssured.given()
				   .contentType("application/json")
				   .body(validUpdateProfile)
				   .when().post("/v1/api/profile/update-profile")
				   .then()
				   .statusCode(200)
				   .body("data.name", is("John Does"))
				   .body("data.id", is(customerId));

		deleteCustomerProfile(customerId);
	}

	@Test
	public void updateCustomerProfile_whenCustomerNotExist_doNotUpdatesProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when().post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla"))
									   .extract().path("data.id");

		String inValidUpdateProfile = String.format(UPDATE_CUSTOMER_PROFILE_TEMPLATE,
													"invalid_customer_id",
													"nikola.tesla@example.com",
													"123456");

		RestAssured.given()
				   .contentType("application/json")
				   .body(inValidUpdateProfile)
				   .when().post("/v1/api/profile/update-profile")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001));

		deleteCustomerProfile(customerId);
	}

	@Test
	public void updateCustomerProfile_whenPasswordUnmatch_doNotUpdatesProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when().post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla"))
									   .extract().path("data.id");

		String inValidUpdateProfile = String.format(UPDATE_CUSTOMER_PROFILE_TEMPLATE,
													customerId,
													"nikola.tesla@example.com",
													"122335");

		RestAssured.given()
				   .contentType("application/json")
				   .body(inValidUpdateProfile)
				   .when().post("/v1/api/profile/update-profile")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001));

		deleteCustomerProfile(customerId);
	}
}
