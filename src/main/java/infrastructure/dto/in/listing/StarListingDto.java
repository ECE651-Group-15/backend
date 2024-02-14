package infrastructure.dto.in.listing;

import domain.listing.StarListing;
import lombok.Builder;

@Builder
public record StarListingDto(String customerId,
                             String listingId) {
    public StarListing toDomain() {
        return StarListing.builder()
                          .customerId(customerId)
                          .listingId(listingId)
                          .build();
    }
}
