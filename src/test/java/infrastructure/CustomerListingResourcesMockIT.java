package infrastructure;

import domain.listing.ListingDetails;
import domain.listing.ListingService;
import domain.profile.CustomerProfileService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerListingResourcesMockIT {

	@InjectMock
	ListingService listingService;

	@InjectMock
	CustomerProfileService customerProfileService;

	@Test
	public void getListingByPage_NoListingsExist_ReturnErrorMessage() {
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

	@Test
	public void getListingWithCustomerDetail_whenCustomerDoesNotExist_returnsError() {
		when(customerProfileService.getCustomerProfile(anyString())).thenReturn(Optional.empty());
		ListingDetails listingDetails = ListingDetails.builder()
		                                              .build();
		String listingId = UUID.randomUUID().toString();
		when(listingService.getListing(anyString())).thenReturn(Optional.of(listingDetails));
		RestAssured.given()
		           .when()
		           .post("/v1/api/listing-profile/listing/" + listingId + "/customer/")
		           .then()
		           .statusCode(200)
		           .body("message", containsString("Cannot find customer with id"));
	}
}
