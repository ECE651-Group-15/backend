package domain.listing;

import java.util.Optional;

public interface ListingRepositoryInterface {
    Optional<ListingDetails> findById(String id);
    ListingDetails save(ListingDetails listing);
    void delete(String id);
}
