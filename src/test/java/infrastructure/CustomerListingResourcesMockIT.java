package infrastructure;

import domain.listing.ListingService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerListingResourcesMockIT {

	@InjectMock
	ListingService listingService;

	@Test
	public void GetListingByPage_NoListingsExist_ReturnErrorMessage() {
		when(listingService.getListingAndCustomerByPage(0, 20)).thenReturn(List.of());
		String VALID_GET_LISTING_PAGE_TEMPLATE = """
				{
				    "page": "%d"
				}
				""";
		String validGetListingByPage = String.format(VALID_GET_LISTING_PAGE_TEMPLATE, 0);
		RestAssured.given()
				   .contentType("application/json")
				   .body(validGetListingByPage)
				   .when()
				   .post("/v1/api/listing-profile/get-listing-page")
				   .then()
				   .statusCode(200)
				   .body("message", containsString("No listings found."));
	}
}
