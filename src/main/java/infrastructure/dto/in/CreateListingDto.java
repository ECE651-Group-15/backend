package infrastructure.dto.in;


import domain.listing.Category;
import domain.listing.CreateListing;
import domain.GeoPosition;
import domain.listing.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Builder
public class CreateListingDto {
    public final String title;
    public final String description;
    public final Optional<Double> price;
    public final Double longitude;
    public final Double latitude;
    public final String category;
    public final String userId;
    public final String status;
    public final List<String> images;

    public CreateListing toDomain() {
        return CreateListing.builder()
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
                            .build();
    }
}
