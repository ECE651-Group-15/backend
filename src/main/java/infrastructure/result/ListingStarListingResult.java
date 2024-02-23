package infrastructure.result;

import domain.listing.ListingDetails;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder(toBuilder = true)
public class ListingStarListingResult {
	boolean listingNotFound;
	boolean customerNotFound;
	Optional<ListingDetails> listingDetails;
}
