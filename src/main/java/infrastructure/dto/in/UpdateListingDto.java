package infrastructure.dto.in;

import domain.Category;
import domain.ListingDetails;
import domain.ListingStatus;
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
    public final String location;
    public final String category;
    public final String userId;
    public final String status;
    public final List<String> images;
    public final Integer starCount;

    public ListingDetails toDomain() {
        return ListingDetails.builder()
                .id(id)
                .title(title)
                .description(description)
                .price(price)
                .location(location)
                .category(Category.valueOf(category))
                .userId(userId)
                .status(ListingStatus.valueOf(status))
                .images(images)
                .starCount(starCount)
                .build();
    }
}
