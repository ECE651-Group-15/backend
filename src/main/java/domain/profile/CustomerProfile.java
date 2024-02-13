package domain.profile;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder(toBuilder = true)
public class CustomerProfile {
    String id;
    String name;
    Optional<String> email;
    Optional<String> phone;
    Optional<Double> longitude;
    Optional<Double> latitude;
    Optional<List<String>> postedListingIds;
    Optional<List<String>> starredListingIds;
}
