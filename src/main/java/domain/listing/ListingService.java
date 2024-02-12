package domain.listing;

import infrastructure.sql.ListingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class ListingService {

    @Inject
    ListingRepository listingRepository;

    public ListingDetails createListing(CreateListing createListing) {
        ListingDetails listingDetails = ListingDetails.builder()
                                                      .id(UUID.randomUUID().toString())
                                                      .title(createListing.getTitle())
                                                      .description(createListing.getDescription())
                                                      .price(createListing.getPrice())
                                                      .latitude(createListing.getLatitude())
                                                      .longitude(createListing.getLongitude())
                                                      .category(createListing.getCategory())
                                                      .userId(createListing.getUserId())
                                                      .status(createListing.getStatus())
                                                      .images(createListing.getImages())
                                                      .starCount(0)
                                                      .createdAt(createListing.getCreatedAt())
                                                      .updatedAt(createListing.getUpdatedAt())
                                                      .build();
        listingRepository.save(listingDetails);
        return listingDetails;
    }

    public ListingDetails updateListing(UpdateListing updateListing) {
        return null;
    }

    public ListingDetails deleteListing(String listingId) {
        return null;
    }

    public String getListing(String listingId) {
        return "hello";
    }

    public ListingDetails[] getListings() {
        return null;
    }
}
