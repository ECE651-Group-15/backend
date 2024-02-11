package domain.profile;

import java.util.Optional;

public interface CustomerProfileRepository {
    void save(CustomerProfile customerProfile);
    Optional<CustomerProfile> getCustomerProfile(String id);
    Optional<CustomerProfile> updateCustomerProfile(CustomerProfile customerProfile);
    Optional<CustomerProfile> deleteCustomerProfile(CustomerProfile customerProfile);
}
