package domain.listing;

import infrastructure.sql.entity.ListingEntity;

import java.util.Optional;

public interface ListingRepositoryInterface {
    Optional<ListingEntity> getListing(String id);
    void save(ListingDetails listing);
    Optional<ListingDetails> delete(ListingDetails listingDetails);
}
