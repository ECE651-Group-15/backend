package infrastructure;

import domain.listing.Category;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
@QuarkusTest
public class CustomerListingResourcesIT {
    private String customerId;
    private String listingId;

    private final String VALID_STARLISTING_TEMPLATE= """
            {
                "customerId": "%s",
                "listingId": "%s"
            }
            """;
    private final String VALID_CUSTOMER_PROFILE_TEMPLATE = """
			{
			  "name": "Nikola Tesla1",
			  "email": "nikola.tesla1@example.com",
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
    private void deleteCustomerProfile(String customerId) {
        RestAssured.given()
                .contentType("application/json")
                .when().post("/v1/api/profile/delete-profile/" + customerId)
                .then()
                .statusCode(200);
    }


    @Test
    public void StarListing_whenCustomerNotExists_returnErrorMessage(){
        String validListing = String.format(VALID_STARLISTING_TEMPLATE, "123456","123456");
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
    public void StarListing_whenListingNotExists_returnErrorMessage(){
        String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");

        String customerId = RestAssured.given()
                .contentType("application/json")
                .body(validProfile)
                .when()
                .post("/v1/api/profile/create-profile")
                .then()
                .statusCode(200)
                .body("data.name", is("Nikola Tesla1"))
                .extract().path("data.id");
        System.out.println("customerId :" + customerId);
        String validStarListing = String.format(VALID_STARLISTING_TEMPLATE,customerId,"123456");

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
                .body("message", containsString("Cannot find listing with id " + "123456" + "."));

        deleteCustomerProfile(customerId);



    }
    @Test
    public void StarListing_whenListingExists_returnListingDetails(){


        String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");

        String customerId = RestAssured.given()
                .contentType("application/json")
                .body(validProfile)
                .when()
                .post("/v1/api/profile/create-profile")
                .then()
                .statusCode(200)
                .body("data.name", is("Nikola Tesla1"))
                .extract().path("data.id");
        System.out.println("customerId :" + customerId);

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
        System.out.println("listingId :" + listingId);

        String validStarListing = String.format(VALID_STARLISTING_TEMPLATE,customerId,listingId);

        RestAssured.given()
                .contentType("application/json")
                .body(validStarListing)
                .log().all()
                .when()
                .post("/v1/api/listing-profile/star-listing")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.listingDetails.id",is(listingId))
                .body("data.customerProfilesDetails.id",is(customerId));

        RestAssured.given()
                .when().post("v1/api/listings/delete-listing/" + listingId)
                .then()
                .statusCode(200);
        deleteCustomerProfile(customerId);
    }
    @Test
    public void getCustomerPostedListings_WhenPageLessThanZero_returnErrorMessage(){
        String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");

        String customerId = RestAssured.given()
                .contentType("application/json")
                .body(validProfile)
                .when()
                .post("/v1/api/profile/create-profile")
                .then()
                .statusCode(200)
                .body("data.name", is("Nikola Tesla1"))
                .extract().path("data.id");
        System.out.println("customerId :" + customerId);

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
        System.out.println("listingId :" + listingId);

        String validGetCustomerPostedListing = String.format(VALID_GET_POSTED_LISTING_TEMPLATE,-1,customerId);
        RestAssured.given()
                .contentType("application/json")
                .body(validGetCustomerPostedListing)
                .when()
                .post("/v1/api/listing-profile/get-customer-posted-listings")
                .then()
                .statusCode(200)
                .body("code",is(4001))
                .body("message",containsString("Page number cannot be negative."));

        RestAssured.given()
                .when().post("v1/api/listings/delete-listing/" + listingId)
                .then()
                .statusCode(200);
        deleteCustomerProfile(customerId);
    }
    @Test
    public void getCustomerPostedListings_WhenCustomerProfileNotExist_ReturnErrorMessage(){
        String validGetCustomerPostedListing = String.format(VALID_GET_POSTED_LISTING_TEMPLATE,0,"non-existing-customerID");
        RestAssured.given()
                .contentType("application/json")
                .body(validGetCustomerPostedListing)
                .when()
                .post("/v1/api/listing-profile/get-customer-posted-listings")
                .then()
                .statusCode(200)
                .body("code",is(4001))
                .body("message",containsString("Cannot find customer with id "));
    }
    @Test
    public void getCustomerPostedListings_WhenCustomerExist_ReturnListingDetailsInPage(){
        String validProfile = String.format(VALID_CUSTOMER_PROFILE_TEMPLATE, "123456");

        String customerId = RestAssured.given()
                .contentType("application/json")
                .body(validProfile)
                .when()
                .post("/v1/api/profile/create-profile")
                .then()
                .statusCode(200)
                .body("data.name", is("Nikola Tesla1"))
                .extract().path("data.id");
        System.out.println("customerId :" + customerId);

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
        System.out.println("listingId :" + listingId);

        String validGetCustomerPostedListing = String.format(VALID_GET_POSTED_LISTING_TEMPLATE,0,customerId);
        RestAssured.given()
                .log().all()
                .contentType("application/json")
                .body(validGetCustomerPostedListing)
                .when()
                .post("/v1/api/listing-profile/get-customer-posted-listings")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.postedListings[0].id",is(listingId))
                .body("data.postedListings[0].customerId",is(customerId));



        RestAssured.given()
                .when().post("v1/api/listings/delete-listing/" + listingId)
                .then()
                .statusCode(200);
        deleteCustomerProfile(customerId);
    }




}
