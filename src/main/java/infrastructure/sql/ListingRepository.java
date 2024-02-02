package infrastructure.sql;

import domain.listing.ListingDetails;
import domain.listing.ListingRepositoryInterface;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ListingRepository implements ListingRepositoryInterface {
    public Optional<ListingDetails> findById(String id) {
        return Optional.empty();
    }

    public ListingDetails save(ListingDetails listing) {
        return null;
    }

    public void delete(String id) {
    }

}
