package domain.profile;


import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder(toBuilder = true)
public class CustomerProfile {
    String id;
    String name;
    Optional<String> email;
    Optional<String> phone;
    Optional<Double> longitude;
    Optional<Double> latitude;
//    Optional<ListingDetails[]> savedListings;
    // starred listing
    Optional<List<String>> starredListingIds;

}
