package domain.listing;

import infrastructure.sql.entity.ListingEntity;

import java.util.List;
import java.util.Optional;

public interface ListingRepository {
    void save(ListingEntity listingEntity);
    Optional<ListingEntity> getListing(String id);
    List<ListingEntity> getListingPage(int pageNumber, int pageSizeIn);
	List<ListingEntity> getListingByTitle(String title);
    Optional<ListingEntity> updateListing(UpdateListing updateListing);
    Optional<ListingEntity> deleteListing(ListingEntity listingEntity);
}
