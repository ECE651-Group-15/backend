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
public class ListingResourcesIT {
    private String customerId;
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

    @AfterEach
    public void tearDown() {
        RestAssured.given()
                   .when().post("/v1/api/profile/delete-profile/" + customerId)
                   .then()
                   .statusCode(200);
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
}
