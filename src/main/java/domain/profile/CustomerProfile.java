package domain.profile;

import domain.listing.ListingDetails;
import lombok.Data;

import java.util.Optional;

@Data
public class CustomerProfile {
    String id;
    String name;
    Optional<String> email;
    Optional<String> phone;
    Optional<Double> longitude;
    Optional<Double> latitude;
    ListingDetails[] savedListings;
}
