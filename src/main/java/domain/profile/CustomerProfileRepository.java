package domain.profile;

import infrastructure.sql.entity.CustomerProfileEntity;

import java.util.Optional;

public interface CustomerProfileRepository {
    void save(CustomerProfileEntity customerProfile);
    Optional<CustomerProfileEntity> getCustomerProfile(String id);
    Optional<CustomerProfileEntity> updateCustomerProfile(CustomerProfileEntity customerProfile);
    Optional<CustomerProfileEntity> deleteCustomerProfile(CustomerProfileEntity customerProfile);

    Optional<CustomerProfileEntity> getCustomerProfileByEmail(String email);
}
