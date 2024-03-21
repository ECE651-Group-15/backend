package infrastructure;

import domain.listing.Category;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;



import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;


@QuarkusTest
public class CustomerListingResourcesIT {
    private String listingId;

    private final String VALID_STAR_LISTING_TEMPLATE = """
            {
                "customerId": "%s",
                "listingId": "%s"
            }
            """;
    private final String VALID_CUSTOMER_PROFILE_TEMPLATE = """
			{
			  "name": "%s",
			  "email": "%s",
			  "password": "%s",
			  "phone": "1234567890"
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
    private final String VALID_GET_POSTED_LISTING_TEMPLATE= """
            {
                "page": "%d",
                "customerId": "%s"
            }
            """;
    private final String VALID_STARRED_LISTINGS_TEMPLATE= """
            {
                "customerId": "%s",
                "page": "%d",
                "pageSize": "%d"

            }
            """;
    private final String VALID_GET_LISTING_PAGE_TEMPLATE= """
            {
                "page": "%d"
            }
            """;








	private final String VALID_GET_COMPLETED_LISTING_TEMPLATE = """
        {
            "page": %d,
            "pageSize": %d,
            "customerId": "%s"
        }
        """;





	








	private void deleteCustomerProfile(String customerId) {
        RestAssured.given()
				   .contentType("application/json")
				   .when().post("/v1/api/profile/delete-profile/" + customerId)
				   .then()
				   .statusCode(200)
				   .body("data.id", is(customerId));
    }


    @Test
    public void starListing_whenCustomerNotExists_returnErrorMessage(){
        String validListing = String.format(VALID_STAR_LISTING_TEMPLATE, "123456", "123456");
         RestAssured.given()
					.contentType("application/json")
					.body(validListing)
					.when()
					.post("/v1/api/listing-profile/star-listing")
					.then()
					.statusCode(200)
					.body("code", is(4001))
					.body("message", containsString("Cannot find customer with id " + "123456" + "."));
	}

    @Test
    public void starListing_whenListingNotExists_returnErrorMessage(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
        String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

        String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is(expectedName))
									   .extract().path("data.id");

		String randomListingId = UUID.randomUUID().toString();
        String validStarListing = String.format(VALID_STAR_LISTING_TEMPLATE, customerId, randomListingId);

        RestAssured.given()
				   .contentType("application/json")
				   .body(validStarListing)
				   .log().all()
				   .when()
				   .post("/v1/api/listing-profile/star-listing")
				   .then()
				   .log().all()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Cannot find listing with id " + randomListingId + "."));

        deleteCustomerProfile(customerId);
    }
    @Test
    public void starListing_whenListingExists_returnListingDetails(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

        String customerId = RestAssured.given()
                .contentType("application/json")
                .body(validProfile)
                .when()
                .post("/v1/api/profile/create-profile")
                .then()
                .statusCode(200)
                .body("data.name", is(expectedName))
                .extract().path("data.id");

        String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);

        listingId = RestAssured.given()
							   .contentType("application/json")
							   .body(validListing)
							   .when().post("/v1/api/listings/create-listing")
							   .then()
							   .statusCode(200)
							   .body("data.customerId", is(customerId))
							   .extract()
							   .path("data.id");

        String validStarListing = String.format(VALID_STAR_LISTING_TEMPLATE, customerId, listingId);

        RestAssured.given()
				   .contentType("application/json")
				   .body(validStarListing)
				   .log().all()
				   .when()
				   .post("/v1/api/listing-profile/star-listing")
				   .then()
				   .log().all()
				   .statusCode(200)
				   .body("data.listingDetails.id", is(listingId))
				   .body("data.customerProfilesDetails.id",is(customerId));

        RestAssured.given()
				   .when().post("v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200);

        deleteCustomerProfile(customerId);
    }

	@Test
	public void unStarListing_whenCustomerNotExists_returnErrorMessage(){
		String validListing = String.format(VALID_STAR_LISTING_TEMPLATE, "123456", "123456");
		RestAssured.given()
				   .contentType("application/json")
				   .body(validListing)
				   .when()
				   .post("/v1/api/listing-profile/unstar-listing")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Cannot find customer with id " + "123456" + "."));
	}

	@Test
	public void unStarListing_whenListingNotExists_returnErrorMessage(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is(expectedName))
									   .extract().path("data.id");

		String randomListingId = UUID.randomUUID().toString();
		String validStarListing = String.format(VALID_STAR_LISTING_TEMPLATE, customerId, randomListingId);

		RestAssured.given()
				   .contentType("application/json")
				   .body(validStarListing)
				   .log().all()
				   .when()
				   .post("/v1/api/listing-profile/unstar-listing")
				   .then()
				   .log().all()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Cannot find listing with id " + randomListingId + "."));

		deleteCustomerProfile(customerId);
	}
	@Test
	public void unStarListing_whenListingExists_returnUnStarredListing(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is(expectedName))
									   .extract().path("data.id");

		String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);

		listingId = RestAssured.given()
							   .contentType("application/json")
							   .body(validListing)
							   .when().post("/v1/api/listings/create-listing")
							   .then()
							   .statusCode(200)
							   .body("data.customerId", is(customerId))
							   .extract()
							   .path("data.id");

		String validStarListing = String.format(VALID_STAR_LISTING_TEMPLATE, customerId, listingId);

		RestAssured.given()
				   .contentType("application/json")
				   .body(validStarListing)
				   .log().all()
				   .when()
				   .post("/v1/api/listing-profile/star-listing")
				   .then()
				   .log().all()
				   .statusCode(200)
				   .body("data.listingDetails.id", is(listingId))
				   .body("data.customerProfilesDetails.id",is(customerId));

		RestAssured.given()
				   .contentType("application/json")
				   .body(validStarListing)
				   .log().all()
				   .when()
				   .post("/v1/api/listing-profile/unstar-listing")
				   .then()
				   .log().all()
				   .statusCode(200)
				   .body("data.listingDetails.id", is(listingId))
				   .body("data.customerProfilesDetails.id",is(customerId));

		RestAssured.given()
				   .when().post("v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200);

		deleteCustomerProfile(customerId);
	}

    @Test
    public void getCustomerPostedListings_WhenPageLessThanZero_returnErrorMessage(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

        String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla1"))
									   .extract().path("data.id");

        String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);

        listingId = RestAssured.given()
							   .contentType("application/json")
							   .body(validListing)
							   .when().post("/v1/api/listings/create-listing")
							   .then()
							   .statusCode(200)
							   .body("data.customerId", is(customerId))
							   .extract()
							   .path("data.id");

        String validGetCustomerPostedListing = String.format(VALID_GET_POSTED_LISTING_TEMPLATE, -1, customerId);
		RestAssured.given()
				   .contentType("application/json")
				   .body(validGetCustomerPostedListing)
				   .when()
				   .post("/v1/api/listing-profile/get-customer-posted-listings")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message",containsString("Page number cannot be negative."));

        RestAssured.given()
				   .when().post("v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200);
		deleteCustomerProfile(customerId);
    }

    @Test
    public void getCustomerPostedListings_WhenCustomerProfileNotExist_ReturnErrorMessage(){
        String validGetCustomerPostedListing = String.format(VALID_GET_POSTED_LISTING_TEMPLATE, 0, "non-existing-customerID");
		RestAssured.given()
				   .contentType("application/json")
				   .body(validGetCustomerPostedListing)
				   .when()
				   .post("/v1/api/listing-profile/get-customer-posted-listings")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message",containsString("Cannot find customer with id "));
    }

    @Test
    public void getCustomerPostedListings_WhenCustomerExist_ReturnListingDetailsInPage(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

        String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla1"))
									   .extract().path("data.id");

        String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);

        listingId = RestAssured.given()
							   .contentType("application/json")
							   .body(validListing)
							   .when().post("/v1/api/listings/create-listing")
							   .then()
							   .statusCode(200)
							   .body("data.customerId", is(customerId))
							   .extract()
							   .path("data.id");

        String validGetCustomerPostedListing = String.format(VALID_GET_POSTED_LISTING_TEMPLATE, 0, customerId);
		RestAssured.given()
				   .contentType("application/json")
				   .body(validGetCustomerPostedListing)
				   .when()
				   .post("/v1/api/listing-profile/get-customer-posted-listings")
				   .then()
				   .statusCode(200)
				   .body("data.postedListings[0].id", is(listingId))
				   .body("data.postedListings[0].customerId",is(customerId));

        RestAssured.given()
				   .when().post("v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200);
		deleteCustomerProfile(customerId);
    }












	@Test
	public void getCustomerCompletedListings_WhenPageIsNegative_ReturnErrorMessage() {
		String invalidRequest = String.format(VALID_GET_COMPLETED_LISTING_TEMPLATE, -1, 10, "validCustomerId");

		RestAssured.given()
				.contentType("application/json")
				.body(invalidRequest)
				.when()
				.post("/v1/api/listing-profile/get-customer-completed-listings")
				.then()
				.statusCode(200)
				.body("code", is(4001))
				.body("message", containsString("Page number cannot be negative."));
	}

	@Test
	public void getCustomerCompletedListings_WhenCustomerNotExists_ReturnErrorMessage() {
		String invalidRequest = String.format(VALID_GET_COMPLETED_LISTING_TEMPLATE, 0, 10, "non-existing-customerId");

		RestAssured.given()
				.contentType("application/json")
				.body(invalidRequest)
				.when()
				.post("/v1/api/listing-profile/get-customer-completed-listings")
				.then()
				.statusCode(200)
				.body("code", is(4001))
				.body("message", containsString("Cannot find customer with id non-existing-customerId."));
	}


	@Test
	public void getCustomerCompletedListings_WhenCustomerExists_ReturnCompletedListings() {
		String validCustomerId = "validCustomerId";

		String validRequest = String.format(VALID_GET_COMPLETED_LISTING_TEMPLATE, 0, 10, validCustomerId);

		RestAssured.given()
				.contentType("application/json")
				.body(validRequest)
				.when()
				.post("/v1/api/listing-profile/get-customer-completed-listings")
				.then()
				.statusCode(200)
				.body("data.completedListings", not(empty()));
	}

	@Test
	public void getCustomerCompletedListings_WhenCustomerHasCompletedListings_ReturnListings() {
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Test Customer";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName, email, "password");
		String customerId = RestAssured.given()
				.contentType("application/json")
				.body(validProfile)
				.when()
				.post("/v1/api/profile/create-profile")
				.then()
				.statusCode(200)
				.extract().path("data.id");

		String inactiveListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);
		// change status from ACTIVE to INACTIVE
		inactiveListing = inactiveListing.replace("\"status\": \"ACTIVE\"", "\"status\": \"INACTIVE\"");

		String listingId = RestAssured.given()
				.contentType("application/json")
				.body(inactiveListing)
				.when().post("/v1/api/listings/create-listing")
				.then()
				.statusCode(200)
				.extract()
				.path("data.id");

		String validRequest = String.format(VALID_GET_COMPLETED_LISTING_TEMPLATE, 0, 10, customerId);
		RestAssured.given()
				.contentType("application/json")
				.body(validRequest)
				.when()
				.post("/v1/api/listing-profile/get-customer-completed-listings")
				.then()
				.statusCode(200)
				.body("data.completedListings.size()", is(1))
				.body("data.completedListings[0].id", is(listingId));

		RestAssured.given()
				.when().post("v1/api/listings/delete-listing/" + listingId)
				.then()
				.statusCode(200);
		deleteCustomerProfile(customerId);
	}



















	@Test
    public void getStarredListings_WhenPageIsNegative_ReturnErrorMessage(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

        String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla1"))
									   .extract().path("data.id");

        String validGetStarredListing = String.format(VALID_STARRED_LISTINGS_TEMPLATE, customerId, -1, 1);
        RestAssured.given()
				   .log().all()
				   .contentType("application/json")
				   .body(validGetStarredListing)
				   .when()
				   .post("/v1/api/listing-profile/starred-listings")
				   .then()
				   .log().all()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message",containsString("Page number cannot be negative."));

        deleteCustomerProfile(customerId);

    }

    @Test
    public void getStarredListings_WhenCustomerNotExists_ReturnErrorMessage(){
        String validGetStarredListing = String.format(VALID_STARRED_LISTINGS_TEMPLATE,"Non-existing-customerID",0,1);
        RestAssured.given()
				   .contentType("application/json")
				   .body(validGetStarredListing)
				   .when()
				   .post("/v1/api/listing-profile/starred-listings")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Cannot find customer with id "));
	}

    @Test
    public void getStarredListings_WhenCustomerExists_ReturnListingDetails(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

        String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla1"))
									   .extract().path("data.id");

        String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);

        listingId = RestAssured.given()
							   .contentType("application/json")
							   .body(validListing)
							   .when().post("/v1/api/listings/create-listing")
							   .then()
							   .statusCode(200)
							   .body("data.customerId", is(customerId))
							   .extract()
							   .path("data.id");

        String validStarListing = String.format(VALID_STAR_LISTING_TEMPLATE, customerId, listingId);

        RestAssured.given()
				   .contentType("application/json")
				   .body(validStarListing)
				   .when()
				   .post("/v1/api/listing-profile/star-listing")
				   .then()
				   .statusCode(200)
				   .body("data.listingDetails.id", is(listingId))
				   .body("data.customerProfilesDetails.id", is(customerId));

		String validGetStarredListing = String.format(VALID_STARRED_LISTINGS_TEMPLATE, customerId, 0, 1);
		RestAssured.given()
				   .contentType("application/json")
				   .body(validGetStarredListing)
				   .when()
				   .post("/v1/api/listing-profile/starred-listings")
				   .then()
				   .statusCode(200)
				   .body("data.starredListings[0].id", is(listingId))
				   .body("data.starredListings[0].customerId", is(customerId));


		RestAssured.given()
				   .when().post("v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200);
		deleteCustomerProfile(customerId);
    }

    @Test
    public void GetListingByPage_pageNumberIsNegative_ReturnErrorMessage(){
        String validGetListingByPage = String.format(VALID_GET_LISTING_PAGE_TEMPLATE, -1);
        RestAssured.given()
				   .contentType("application/json")
				   .body(validGetListingByPage)
				   .when()
				   .post("/v1/api/listing-profile/get-listing-page")
				   .then()
				   .statusCode(200)
				   .body("code", is(4001))
				   .body("message", containsString("Page number and page size cannot be negative."));
	}


    @Test
    public void GetListingByPage_ListingsExist_ReturnListingDetails(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName, email, "123456");

		String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla1"))
									   .extract().path("data.id");

		String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);

		listingId = RestAssured.given()
							   .contentType("application/json")
							   .body(validListing)
							   .when().post("/v1/api/listings/create-listing")
							   .then()
							   .statusCode(200)
							   .body("data.customerId", is(customerId))
							   .extract()
							   .path("data.id");

		String validGetListingByPage = String.format(VALID_GET_LISTING_PAGE_TEMPLATE, 0);
		RestAssured.given()
				   .contentType("application/json")
				   .body(validGetListingByPage)
				   .when()
				   .post("/v1/api/listing-profile/get-listing-page")
				   .then()
				   .log().all()
				   .statusCode(200)
				   .body("data.listingDetails[0].listingDetails.id", is(listingId))
				   .body("data.listingDetails[0].customerProfilesDetails.id", is(customerId));

		RestAssured.given()
				   .when().post("v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200);
		deleteCustomerProfile(customerId);
    }

    @Test
    public void GetListingWithCustomerDetails_WhenListingIDNotExists_ReturnErrorMessage(){
        RestAssured.given()
				   .when()
				   .post("/v1/api/listing-profile/listing/" + "Non-Existing-ListingID" + "/customer/")
				   .then()
				   .statusCode(200)
				   .body("message", containsString("Cannot find listing with id " + "Non-Existing-ListingID" + "."));
	}

    @Test
    public void GetListingWithCustomerDetails_WhenListingExists_ReturnListingDetails(){
		String email = UUID.randomUUID() + "@example.com";
		String expectedName = "Nikola Tesla1";
		String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, expectedName ,email, "123456");

        String customerId = RestAssured.given()
									   .contentType("application/json")
									   .body(validProfile)
									   .when()
									   .post("/v1/api/profile/create-profile")
									   .then()
									   .statusCode(200)
									   .body("data.name", is("Nikola Tesla1"))
									   .extract().path("data.id");

		String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);

		listingId = RestAssured.given()
							   .contentType("application/json")
							   .body(validListing)
							   .when().post("/v1/api/listings/create-listing")
							   .then()
							   .statusCode(200)
							   .body("data.customerId", is(customerId))
							   .extract()
							   .path("data.id");

		RestAssured.given()
				   .when()
				   .post("/v1/api/listing-profile/listing/" + listingId + "/customer/")
				   .then()
				   .statusCode(200)
				   .body("data.listingDetails.id", is(listingId))
				   .body("data.customerProfilesDetails.id", is(customerId));

		RestAssured.given()
				   .when().post("v1/api/listings/delete-listing/" + listingId)
				   .then()
				   .statusCode(200);
		deleteCustomerProfile(customerId);
	}

}
