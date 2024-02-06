package infrastructure.sql;

import domain.listing.ListingDetails;
import domain.listing.ListingRepositoryInterface;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ListingRepository implements ListingRepositoryInterface, PanacheRepository<ListingEntity> {

    public void save(ListingDetails listing) {
        ListingEntity listingEntity = ListingEntity.fromDomain(listing);
        try {
            persist(listingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure saving listing to db", e);
        }
    }

    public Optional<ListingDetails> findById(String id) {
        return Optional.empty();
    }

    public Optional<ListingDetails> delete(String id) {
        return Optional.empty();
    }

}
