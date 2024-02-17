package infrastructure.sql;

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
    public Optional<CustomerProfileEntity> updateCustomerProfile(CustomerProfileEntity customerProfileEntity) {
        CustomerProfileEntity.updateFromEntity(customerProfileEntity);
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
}
