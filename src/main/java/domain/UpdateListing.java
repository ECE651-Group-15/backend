package domain;

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
    String location;
    String category;
    String userId;
    String status;
    List<String> images;
    Integer starCount;
}
