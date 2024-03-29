package domain.profile;

import infrastructure.sql.entity.CustomerProfileEntity;

import java.util.List;
import java.util.Optional;

public interface CustomerProfileRepository {
    void save(CustomerProfileEntity customerProfile);
    Optional<CustomerProfileEntity> getCustomerProfile(String id);
    Optional<CustomerProfileEntity> updateCustomerProfile(UpdateCustomerProfile updateCustomerProfile);
    Optional<CustomerProfileEntity> deleteCustomerProfile(CustomerProfileEntity customerProfile);
    Optional<CustomerProfileEntity> getCustomerProfileByEmail(String email);
    List<CustomerProfileEntity> getCustomerProfileByPage(int pageNumber, int pageSize);
}
