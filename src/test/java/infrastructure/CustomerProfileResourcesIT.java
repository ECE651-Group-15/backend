package infrastructure;

import domain.listing.Category;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;

@QuarkusTest
public class CustomerProfileResourcesIT {
	private final String VALID_CUSTOMER_PROFILE_TEMPLATE = """
			{
			  "name": "Nikola Tesla",
			  "email": "%s@example.com",
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
	private final String PAGE_DTO = """
			{
				"page": %d,
				"pageSize": %d
			}
			""";
	private final String VALID_LISTING_TEMPLATE = """
			{
			  "title": "Vintage Record Player",
			  "description": "1960s turntable in working condition. Includes a collection of jazz vinyl.",
			  "price": 250,
			  "longitude": -0.118092,
			  "latitude": 51.509865,
			  "category": "%s",
			  "customerId": "%s",
			  "status": "ACTIVE",
			  "images": []
			}
			""";

	private void deleteCustomerProfile(String customerId) {
		RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/profile/delete-profile/" + customerId)
				   .then()
				   .statusCode(200);
	}

	private void deleteListing(String listingId) {
		RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200)
				   .body("data.id", is(listingId));
	}

	@Test
	public void createCustomerProfile_whenAllInfoProvided_createsProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");
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
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"");
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
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");
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
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");
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
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");

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
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");

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
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");

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

	//	@Test
//	public void updateCustomerProfile_whenCustomerNotExists_ReturnErrorMessage(){
//		String inValidUpdateProfile = String.format(UPDATE_CUSTOMER_PROFILE_TEMPLATE,
//													UUID.randomUUID(),
//													"nikola.tesla@example.com",
//													"122335");
//		RestAssured.given()
//				   .contentType("application/json")
//				   .body(inValidUpdateProfile)
//				   .when().post("/v1/api/profile/update-profile/" )
//				   .then()
//				   .statusCode(200)
//				   .body("code", is(4001))
//				   .body("message", containsString("Cannot find customer with id "));
//	}
	@Test
	public void deleteCustomerProfile_whenCustomerHasPostedListings_doNotDeleteProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when().post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla"))
									   .extract().path("data.id");

		String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);
		String listingId = RestAssured.given()
									  .contentType("application/json")
									  .body(validListing)
									  .when().post("/v1/api/listings/create-listing")
									  .then()
									  .statusCode(200)
									  .body("data.customerId", is(customerId))
									  .extract()
									  .path("data.id");

		RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/profile/delete-profile/" + customerId)
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Cannot delete"));

		deleteListing(listingId);
		deleteCustomerProfile(customerId);
	}

	@Test
	public void deleteCustomerProfile_whenCustomerDoesExistWithNoPostedListing_deleteProfile() {
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE,
											UUID.randomUUID(),
											"123456");

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
				   .when().post("/v1/api/profile/delete-profile/" + customerId)
				   .then()
				   .statusCode(200)
				   .body("data.id", is(customerId));

		deleteCustomerProfile(customerId);
	}

	@Test
	public void deleteCustomerProfile_whenCustomerNotExist_promotesMessage() {
		RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/profile/delete-profile/" + "customerId")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001));
	}

	@Test
	public void getCustomerProfileByPage_whenPageIsInvalid_returnsErrorMessage() {
		String InValidPageDTO = String.format(PAGE_DTO, -1, 40);
		RestAssured.given()
				   .contentType("application/json")
				   .body(InValidPageDTO)
				   .when().post("/v1/api/profile/get-profile/page")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Page number and page size cannot be negative."));
	}

	@Test
	public void getCustomerProfileByPage_WhenCustomerExist_ReturnsPage() {
		final String VALID_CUSTOMER_PROFILE = """
				{
				  "name": "%s",
				  "email": "%s@example.com",
				  "password": "%s",
				  "phone": "1234567890"
				}
				""";
		String name = UUID.randomUUID().toString();
		String validProfile = String.format(VALID_CUSTOMER_PROFILE,
											name,
											UUID.randomUUID(),
											"123456");
		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is(name))
									   .extract().path("data.id");
		String ValidPageDTO = String.format(PAGE_DTO, 0, 40);
		RestAssured.given()
				   .contentType("application/json")
				   .body(ValidPageDTO)
				   .when().post("/v1/api/profile/get-profile/page")
				   .then()
				   .statusCode(200)
				   .body("data.customerProfiles.id", hasItem(customerId));
		deleteCustomerProfile(customerId);
	}

	@Test
	public void getCustomerProfileByPage_internal_whenCustomerExist_returnsProfile() {
		final String VALID_CUSTOMER_PROFILE = """
				{
				  "name": "%s",
				  "email": "%s@example.com",
				  "password": "%s",
				  "phone": "1234567890"
				}
				""";

		String name = UUID.randomUUID().toString();

		String validProfile = String.format(VALID_CUSTOMER_PROFILE,
											name,
											UUID.randomUUID(),
											"123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is(name))
									   .extract().path("data.id");


		RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/profile/get-profile/internal/" + customerId)
				   .then()
				   .statusCode(200)
				   .body("data.id", is(customerId))
				   .body("data.name", is(name));

		deleteCustomerProfile(customerId);
	}

	@Test
	public void getCustomerProfileByPage_internal_whenCustomerDoesNotExist_returnsProfile() {
		String customerId = UUID.randomUUID().toString();

		RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/profile/get-profile/internal/" + customerId)
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", is("Cannot find customer with id " + customerId + "."));
	}
}
