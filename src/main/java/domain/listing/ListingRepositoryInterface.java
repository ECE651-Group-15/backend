package domain.listing;

import infrastructure.sql.entity.ListingEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import java.util.Optional;

public interface ListingRepositoryInterface {
    Optional<ListingEntity> getListing(String id);
    void save(ListingDetails listing);
    Optional<ListingDetails> delete(ListingDetails listingDetails);

    Optional<ListingDetails> updateListing(ListingDetails listing);

    PanacheQuery<ListingEntity> findByUserId(String userId, Page page);

}
