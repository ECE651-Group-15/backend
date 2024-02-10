package domain.listing;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Data
@Builder(toBuilder = true)
public class ListingDetails {
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
    long createdAt;
    long updatedAt;
}
