package infrastructure;

import domain.listing.Category;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class ListingResourcesIT {
    private String customerId;
    private String listingId;
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
    private final String VALID_UPDATE_LISTING_TEMPLATE = """

            {
              "id": "%s",
              "title": "validtitle",
              "description": "%s",
              "price": 250,
              "longitude": -0.118092,
              "latitude": 51.509865,
              "category": "%s",
              "customerId": "%s",
              "status": "ACTIVE",
              "images": []
            }
            """;


    @BeforeEach
    public void setup() {
        String customerProfile = """
                {
                  "name": "John Does",
                  "email": "john.doe@example.com",
                  "phone": "1234567890"
                }
                """;

        customerId = RestAssured.given()
                                .contentType("application/json")
                                .body(customerProfile)
                                .when().post("/v1/api/profile/create-profile")
                                .then()
                                .statusCode(200).extract()
                                .path("data.id");



    }

    private void createListing() {
        String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);
        listingId = RestAssured.given()
                .log().all()
                .contentType("application/json")
                .body(validListing)
                .when().post("/v1/api/listings/create-listing")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.customerId", is(customerId))
                .extract()
                .path("data.id");
        }


    @AfterEach
    public void tearDown() {
        System.out.println("Deleting customer profile with ID: " + customerId);
        RestAssured.given()
                .when().post("v1/api/listings/delete-listing/" + listingId)
                .then()
                .statusCode(200);

        RestAssured.given()
                .when().post("/v1/api/profile/delete-profile/" + customerId)
                .then()
                .statusCode(200)
                .log().all()
                .body("data.id", is(customerId));
        RestAssured.given()
                .when().post("/v1/api/profile/get-profile/" + customerId)
                .then()
                .statusCode(200)
                .log().all()
                .body("code", is(4001));

    }

    @Test
    public void createListing_whenCustomerProfile_notExist_notCreatingListing() {
        String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, "non-existed-customer-id");
        RestAssured.given()
                   .contentType("application/json")
                   .body(validListing)
                   .when().post("/v1/api/listings/create-listing")
                   .then()
                   .statusCode(200)
                   .body("code", is(4001));
    }

    @Test
    public void createListing_whenCustomerProfileExists_createsListing() {

        String validListing = String.format(VALID_LISTING_TEMPLATE, Category.OTHER, customerId);
        RestAssured.given()
                   .contentType("application/json")
                   .body(validListing)
                   .when().post("/v1/api/listings/create-listing")
                   .then()
                   .statusCode(200)
                   .body("data.customerId", is(customerId));
    }

    @Test
    public void createListing_whenCategory_notExist_returnsError() {

        String validListing = String.format(VALID_LISTING_TEMPLATE, "Not valid category", customerId);
        RestAssured.given()
                   .contentType("application/json")
                   .body(validListing)
                   .when().post("/v1/api/listings/create-listing")
                   .then()
                   .statusCode(200)
                   .body("code", is(4001))
                   .body("message", containsString("No enum constant"));
    }
    @Test
    public void getListing_whenListingDoesNotExist_returnsError() {

        RestAssured.given()
                .when().post("/v1/api/listings/get-listing/non-existent-id")
                .then()
                .statusCode(200)
                .body("code", is(4001))
                .body("message", containsString("Cannot find listing with id non-existent-id."));
    }
    @Test
    public void getListing_whenListingExists_returnsListingDetails() {

        RestAssured.given()
                .when().post("/v1/api/listings/get-listing/" + listingId)
                .then()
                .statusCode(200)
                .body("data.id", is(listingId))
                .body("data.customerId", is(customerId));
    }
    @Test
    public void updateListing_whenListingNotExists_ReturnsError(){
        createListing();
        String validListing = String.format(VALID_UPDATE_LISTING_TEMPLATE, "non existing listingID","newTitle",Category.OTHER, customerId);
        RestAssured.given()
                .contentType("application/json")
                .body(validListing)
                .when().post("/v1/api/listings/update-listing")
                .then()
                .statusCode(200)
                .body("code", is(4001))
                .body("message", containsString("Cannot update listing with id non existing listingID as listing was not found with given ID."));

        RestAssured.given()
                .when().post("/v1/api/listings/delete-listing/" + listingId)
                .then()
                .statusCode(200);
    }
    @Test
    public void updateListing_whenListingExists_ReturnsListingDetails(){
        createListing();
        String validListing = String.format(VALID_UPDATE_LISTING_TEMPLATE,listingId,"NewDescription",Category.OTHER, customerId);
        RestAssured.given()
                .contentType("application/json")
                .body(validListing)
                .when().post("/v1/api/listings/update-listing")
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", is(listingId))
                .body("data.customerId", is(customerId))
                //.body("data.title",containsString("validtitle"))
                .body("data.description",containsString("NewDescription"));

    }
//



}
