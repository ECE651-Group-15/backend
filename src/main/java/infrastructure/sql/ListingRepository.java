package infrastructure.sql;

import domain.listing.ListingDetails;
import domain.listing.ListingRepositoryInterface;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ListingRepository implements ListingRepositoryInterface, PanacheRepository<ListingEntity> {
    @Transactional
    public void save(ListingDetails listing) {
        ListingEntity listingEntity = ListingEntity.fromDomain(listing);
        System.out.println(listingEntity);
        try {
            persist(listingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure saving listing to db", e);
        }
    }

    public Optional<ListingDetails> getListing(String listingId) {
        return find("id", listingId).firstResultOptional()
                                    .map(ListingEntity::toDomain);
    }

    public Optional<ListingDetails> delete(String id) {
        return Optional.empty();
    }

}
