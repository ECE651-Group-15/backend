package infrastructure.sql;

import domain.listing.ListingDetails;
import domain.listing.ListingPage;
import domain.profile.CustomerProfile;
import domain.profile.CustomerProfileRepositoryInterface;
import infrastructure.sql.entity.ProfileEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import infrastructure.sql.entity.ListingEntity;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class CustomerProfileRepository implements CustomerProfileRepositoryInterface, PanacheRepository<ProfileEntity> {




    public Optional<ProfileEntity>  findById(String id) {
        return find("id", id).firstResultOptional();
    }

    @Override
    public void save(CustomerProfile profile) {

    }

    @Override
    public Optional<CustomerProfile> delete(CustomerProfile profile) {
        return Optional.empty();
    }

    @Override
    public Optional<CustomerProfile> updateProfile(CustomerProfile profile) {
        return Optional.empty();
    }

    @Override
    public PanacheQuery<ProfileEntity> findByUserId(String userId, Page page) {
        return null;
    }


}
