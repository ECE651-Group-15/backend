package infrastructure.dto.in.listing;

import domain.listing.Category;
import domain.listing.ListingStatus;
import domain.listing.UpdateListing;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder
public record UpdateListingDto(String id,
                               String title,
                               String description,
                               Optional<Double> price,
                               Double latitude,
                               Double longitude,
                               String category,
                               String userId,
                               String status,
                               List<String> images) {
    public UpdateListing toDomain() {
        return UpdateListing.builder()
                            .id(id)
                            .title(title)
                            .description(description)
                            .price(price)
                            .longitude(longitude)
                            .latitude(latitude)
                            .category(Category.valueOf(category))
                            .userId(userId)
                            .status(ListingStatus.valueOf(status))
                            .images(images)
                            .build();
    }
}
