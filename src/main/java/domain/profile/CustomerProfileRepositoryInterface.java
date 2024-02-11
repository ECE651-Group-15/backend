package domain.profile;

import infrastructure.sql.entity.ProfileEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

import java.util.Optional;

public interface CustomerProfileRepositoryInterface {

    Optional<ProfileEntity>  findById(String id);
    void save(CustomerProfile profile);
    Optional<CustomerProfile> delete(CustomerProfile profile);

    Optional<CustomerProfile> updateProfile(CustomerProfile profile);

    PanacheQuery<ProfileEntity> findByUserId(String userId, Page page);
}
