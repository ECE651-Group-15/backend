package infrastructure.sql;

import domain.listing.ListingRepository;
import infrastructure.sql.entity.ListingEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheListingRepository implements ListingRepository, PanacheRepository<ListingEntity> {

    @Override
    @Transactional
    public void save(ListingEntity listingEntity) {
        try {
            persist(listingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure saving listing to db", e);
        }
    }

    @Override
    public Optional<ListingEntity> getListing(String listingId) {
        return find("id", listingId).firstResultOptional();
    }

    @Override
    public List<ListingEntity> getListingPage(int pageNumber, int pageSize) {

        PanacheQuery<ListingEntity> query = find("", Sort.by("updatedAt").descending());

        query.page(Page.of(pageNumber, pageSize));

        return query.list();
    }

    @Override
    @Transactional
    public Optional<ListingEntity> updateListing(ListingEntity listingEntity) {
        ListingEntity.updateFromEntity(listingEntity);
        return Optional.of(listingEntity);
    }

    @Override
    @Transactional
    public Optional<ListingEntity> deleteListing(ListingEntity listingEntity) {
        try {
            delete(listingEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure deleting listing from db", e);
        }
        return Optional.of(listingEntity);
    }
}
