package domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class ListingDetails {
    String id;
    String title;
    String description;
    Optional<Double> price;
    String location;
    Category category;
    String userId;
    ListingStatus status;
    List<String> images;
    int starCount;
    String createdAt;
    String updatedAt;
}
