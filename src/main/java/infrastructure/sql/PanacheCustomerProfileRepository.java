package infrastructure.sql;

import domain.profile.CustomerProfile;
import domain.profile.CustomerProfileRepository;
import infrastructure.sql.entity.CustomerProfileEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class PanacheCustomerProfileRepository implements CustomerProfileRepository, PanacheRepository<CustomerProfileEntity> {

    @Override
    @Transactional
    public void save(CustomerProfile customerProfile) {
        CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.fromDomain(customerProfile);
        try {
            persist(customerProfileEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure saving customer profile to db", e);
        }
    }

    @Override
    public Optional<CustomerProfile> getCustomerProfile(String customerId) {
        return find("id", customerId).firstResultOptional().map(CustomerProfileEntity::toDomain);
    }


    @Override
    @Transactional
    public Optional<CustomerProfile> updateCustomerProfile(CustomerProfile customerProfile) {
        Optional<CustomerProfileEntity> customerProfileEntity = findCustomerProfileById(customerProfile.getId());

        if (customerProfileEntity.isEmpty()) {
            return Optional.empty();
        } else {
            CustomerProfileEntity entity = customerProfileEntity.get();
            CustomerProfileEntity.updateFromEntity(entity);
        }
        return Optional.of(customerProfile);
    }

    @Override
    @Transactional
    public Optional<CustomerProfile> deleteCustomerProfile(CustomerProfile customerProfile) {
        CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.fromDomain(customerProfile);
        try {
            delete(customerProfileEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failure deleting listing from db", e);
        }
        return Optional.of(customerProfile);
    }

    private Optional<CustomerProfileEntity> findCustomerProfileById(String customerId) {
        return find("id", customerId).firstResultOptional();
    }
}
