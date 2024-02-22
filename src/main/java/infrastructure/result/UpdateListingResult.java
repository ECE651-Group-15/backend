package infrastructure.result;

import domain.listing.ListingDetails;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class UpdateListingResult {
    private boolean customerNotFound;
    private boolean listingNotFound;
    private Optional<ListingDetails> updatedListing;
}
