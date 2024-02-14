package domain.listing;

import infrastructure.sql.entity.ListingEntity;

import java.util.Optional;

public interface ListingRepository {
    void save(ListingEntity listingEntity);
    Optional<ListingEntity> getListing(String id);
    Optional<ListingEntity> updateListing(ListingEntity listing);
    Optional<ListingEntity> deleteListing(ListingEntity listingEntity);
}
