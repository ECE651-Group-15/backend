package domain.listing;

import domain.GeoPosition;
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
    GeoPosition position;
    Category category;
    String userId;
    ListingStatus status;
    List<String> images;
    Integer starCount;
}
