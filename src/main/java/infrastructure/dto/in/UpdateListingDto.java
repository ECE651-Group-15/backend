package infrastructure.dto.in;

import domain.listing.Category;
import domain.GeoPosition;
import domain.listing.ListingStatus;
import domain.listing.UpdateListing;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Builder
public class UpdateListingDto {
    public final String id;
    public final String title;
    public final String description;
    public final Optional<Double> price;
    public final Double latitude;
    public final Double longitude;
    public final String category;
    public final String userId;
    public final String status;
    public final List<String> images;
    public final Integer starCount;

    public UpdateListing toDomain() {
        return UpdateListing.builder()
                .id(id)
                .title(title)
                .description(description)
                .price(price)
                .position(GeoPosition.builder()
                                     .longitude(longitude)
                                     .latitude(latitude)
                                     .build())
                .category(Category.valueOf(category))
                .userId(userId)
                .status(ListingStatus.valueOf(status))
                .images(images)
                .starCount(starCount)
                .build();
    }
}
