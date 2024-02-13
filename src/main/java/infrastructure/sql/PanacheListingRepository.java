package infrastructure.sql;

import domain.listing.ListingDetails;
import domain.listing.ListingRepository;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class PanacheListingRepository implements ListingRepository, PanacheRepository<ListingEntity> {

    @Override
    @Transactional
    public void save(ListingDetails listing) {
        ListingEntity listingEntity = ListingEntity.fromDomain(listing);
        try {
            persist(listingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure saving listing to db", e);
        }
    }

    @Override
    public Optional<ListingDetails> getListing(String listingId) {
        return find("id", listingId).firstResultOptional().map(ListingEntity::toDomain);
    }

    @Override
    @Transactional
    public Optional<ListingDetails> updateListing(ListingDetails listing) {
        Optional<ListingEntity> listingEntity = findListingById(listing.getId());

        if (listingEntity.isEmpty()) {
            return Optional.empty();
        } else {
            ListingEntity entity = listingEntity.get();
            ListingEntity.updateFromEntity(entity);
        }
        return Optional.of(listing);
    }

    @Override
    @Transactional
    public Optional<ListingDetails> delete(ListingDetails listingDetails) {
        ListingEntity listingEntity = ListingEntity.fromDomain(listingDetails);
        try {
            delete(listingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure deleting listing from db", e);
        }
        return Optional.of(listingDetails);
    }

    private Optional<ListingEntity> findListingById(String listingId) {
        return find("id", listingId).firstResultOptional();
    }
}