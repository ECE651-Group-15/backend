package infrastructure.sql;

import domain.profile.CustomerProfileRepository;
import domain.profile.UpdateCustomerProfile;
import infrastructure.sql.entity.CustomerProfileEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PanacheCustomerProfileRepository implements CustomerProfileRepository, PanacheRepository<CustomerProfileEntity> {

    @Override
    @Transactional
    public void save(CustomerProfileEntity customerProfileEntity) {
        try {
            persist(customerProfileEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure saving customer profile to db", e);
        }
    }

    public Optional<CustomerProfileEntity> getCustomerProfileByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    @Override
    public Optional<CustomerProfileEntity> getCustomerProfile(String customerId) {
        return find("id", customerId).firstResultOptional();
    }

    @Override
    @Transactional
    public Optional<CustomerProfileEntity> updateCustomerProfile(UpdateCustomerProfile updateCustomerProfile) {
        CustomerProfileEntity customerProfileEntity = getCustomerProfile(updateCustomerProfile.getId()).get();

        customerProfileEntity.setName(updateCustomerProfile.getName());
        customerProfileEntity.setPassword(updateCustomerProfile.getPassword());
		customerProfileEntity.setAvatar(updateCustomerProfile.getAvatar().orElse(customerProfileEntity.getAvatar()));
        customerProfileEntity.setPhone(updateCustomerProfile.getPhone().orElse(customerProfileEntity.getPhone()));
        customerProfileEntity.setLongitude(updateCustomerProfile.getLongitude().orElse(customerProfileEntity.getLongitude()));
        customerProfileEntity.setLatitude(updateCustomerProfile.getLatitude().orElse(customerProfileEntity.getLatitude()));

        return Optional.of(customerProfileEntity);
    }

    @Override
    @Transactional
    public Optional<CustomerProfileEntity> deleteCustomerProfile(CustomerProfileEntity customerProfileEntity) {
        try {
            delete(customerProfileEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure deleting listing from db", e);
        }
        return Optional.of(customerProfileEntity);
    }

    @Override
    public List<CustomerProfileEntity> getCustomerProfileByPage(int pageNumber, int pageSize) {

        PanacheQuery<CustomerProfileEntity> query = find("", Sort.by("name").descending());

        query.page(Page.of(pageNumber, pageSize));

        return query.list();
    }
}
