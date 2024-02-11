package domain.listing;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class UpdateListing {
    String id;
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
}
