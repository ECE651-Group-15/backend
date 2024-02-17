package domain.listing;

import infrastructure.sql.entity.CustomerProfileEntity;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import domain.profile.CustomerProfileRepository;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ListingServiceTest {

    @Inject
    ListingService listingService;

    @InjectMock
    ListingRepository listingRepository;

    @InjectMock
    CustomerProfileRepository customerProfileRepository;



    @Test
    public void createListing_whenCreateListing_profileNotExist_returnEmpty() {
        CreateListing createListing = CreateListing.builder()
                .title("title")
                .description("description")
                .price(Optional.of(0.0))
                .longitude(0.0)
                .latitude(0.0)
                .category(Category.BOOKS)
                .customerId("customerID")
                .status(ListingStatus.ACTIVE)
                .images(Collections.emptyList())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        Mockito.when(customerProfileRepository.getCustomerProfile("customerID")).thenReturn(Optional.empty());
        Optional<ListingDetails> result = listingService.createListing(createListing);
        assertEquals(Optional.empty(), result);
    }
    @Test
    public void createListing_whenCreateListing_profileExist_returnListingDetails(){

        CreateListing createListing = CreateListing.builder()
                .title("title")
                .description("description")
                .price(Optional.of(0.0))
                .longitude(0.0)
                .latitude(0.0)
                .category(Category.BOOKS)
                .customerId("customerID")
                .status(ListingStatus.ACTIVE)
                .images(Collections.emptyList())
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .build();

        CustomerProfileEntity SampleProfile = new CustomerProfileEntity();
        SampleProfile.setId("customerID");

        CustomerProfileEntity mockProfile = Mockito.mock(CustomerProfileEntity.class);
        when(mockProfile.getPostedListings()).thenReturn(new ArrayList<>());
        when(customerProfileRepository.getCustomerProfile("customerID")).thenReturn(Optional.of(mockProfile));

        Optional<ListingDetails> result = listingService.createListing(createListing);
        if (result.isPresent()){
            ListingDetails actuallistingdetails = result.get();
            assertEquals("title",actuallistingdetails.getTitle());
        }
    }
}
