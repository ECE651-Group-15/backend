package infrastructure.dto.in.listing;


import domain.listing.Category;
import domain.listing.CreateListing;
import domain.listing.ListingStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Builder
public record CreateListingDto(String title,
                               String description,
                               Optional<Double> price,
                               Double longitude,
                               Double latitude,
                               String category,
                               String userId,
                               String status,
                               List<String> images) {
    public CreateListing toDomain() {
        return CreateListing.builder()
                            .title(title)
                            .description(description)
                            .price(price)
                            .longitude(longitude)
                            .latitude(latitude)
                            .category(Category.valueOf(category))
                            .userId(userId)
                            .status(ListingStatus.valueOf(status))
                            .images(images)
                            .createdAt(Instant.now().toEpochMilli())
                            .updatedAt(Instant.now().toEpochMilli())
                            .build();
    }
}
