package domain.profile;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder(toBuilder = true)
public class CustomerProfile {
    String id;
    String email;
    String password;
    String name;
    Optional<String> phone;
    Optional<Double> longitude;
    Optional<Double> latitude;
    Optional<List<String>> postedListingIds;
    Optional<List<String>> starredListingIds;
}
