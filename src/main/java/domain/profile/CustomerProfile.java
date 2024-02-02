package domain.profile;

import domain.GeoPosition;
import domain.listing.ListingDetails;
import lombok.Data;

import java.util.Optional;

@Data
public class CustomerProfile {
    String id;
    String name;
    Optional<String> email;
    Optional<String> phone;
    Optional<GeoPosition> position;
    ListingDetails[] savedListings;
}
