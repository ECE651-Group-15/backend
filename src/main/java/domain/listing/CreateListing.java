package domain.listing;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class CreateListing {
    String title;
    String description;
    Optional<Double> price;
    double longitude;
    double latitude;
    Category category;
    String userId;
    ListingStatus status;
    List<String> images;
    int starCount;
    Instant createdAt;
    Instant updatedAt;
}
