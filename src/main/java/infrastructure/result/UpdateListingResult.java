package infrastructure.result;

import domain.listing.ListingDetails;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class UpdateListingResult {
    boolean customerNotFound;
    boolean listingNotFound;
    Optional<ListingDetails> updatedListing;
}
