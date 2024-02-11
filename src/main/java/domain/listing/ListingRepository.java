package domain.listing;

import java.util.Optional;

public interface ListingRepository {
    void save(ListingDetails listing);
    Optional<ListingDetails> getListing(String id);
    Optional<ListingDetails> updateListing(ListingDetails listing);
    Optional<ListingDetails> delete(ListingDetails listingDetails);
}
