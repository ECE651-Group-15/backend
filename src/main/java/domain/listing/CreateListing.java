package domain.listing;

import lombok.Builder;
import lombok.Data;

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
    String customerId;
    ListingStatus status;
    List<String> images;
    long createdAt;
    long updatedAt;
}
