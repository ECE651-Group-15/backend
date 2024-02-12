package domain.listing;

import java.util.Optional;

public interface ListingRepositoryInterface {
    Optional<ListingDetails> findById(String id);
    void save(ListingDetails listing);
    Optional<ListingDetails> delete(String id);
}
