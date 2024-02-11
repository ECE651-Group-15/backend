package infrastructure.sql;

import domain.listing.ListingDetails;
import domain.listing.ListingRepositoryInterface;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class ListingRepository implements ListingRepositoryInterface, PanacheRepository<ListingEntity> {

    @Transactional
    public void save(ListingDetails listing) {
        ListingEntity listingEntity = ListingEntity.fromDomain(listing);
        try {
            persist(listingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure saving listing to db", e);
        }
    }

    public Optional<ListingEntity> getListing(String listingId) {
        return find("id", listingId).firstResultOptional();
    }

    @Transactional
    public Optional<ListingDetails> updateListing(ListingDetails listing) {
        Optional<ListingEntity> listingEntity = getListing(listing.getId());

        if (listingEntity.isEmpty()) {
            return Optional.empty();
        } else {
            ListingEntity entity = listingEntity.get();
            ListingEntity.updateFromEntity(entity);
        }
        return Optional.of(listing);
    }

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

    public PanacheQuery<ListingEntity> findByUserId(String userId, Page page) {
        return find("userId", userId).page(page);
    }


}
