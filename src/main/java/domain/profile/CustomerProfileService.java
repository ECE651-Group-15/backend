package domain.profile;

import infrastructure.sql.entity.CustomerProfileEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerProfileService {
    @Inject
    CustomerProfileRepository customerProfileRepository;

    public CustomerProfile createProfile(CreateCustomerProfile createCustomerProfile) {
        validateProfile(createCustomerProfile);
        CustomerProfile customerProfile = CustomerProfile.builder()
                                                         .id(UUID.randomUUID().toString())
                                                         .name(createCustomerProfile.getName())
                                                         .email(createCustomerProfile.getEmail())
                                                         .phone(createCustomerProfile.getPhone())
                                                         .longitude(createCustomerProfile.getLongitude())
                                                         .latitude(createCustomerProfile.getLatitude())
                                                         .postedListingIds(Optional.of(List.of()))
                                                         .starredListingIds(Optional.of(List.of()))
                                                         .build();
        CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.fromDomain(customerProfile);
        customerProfileRepository.save(customerProfileEntity);
        return customerProfile;
    }

    private void validateProfile(CreateCustomerProfile createCustomerProfile) {
        if (createCustomerProfile.getName() == null || createCustomerProfile.getName().trim().isEmpty()) {
            throw new BadRequestException("Name is required for a profile");
        }
    }

    public Optional<CustomerProfile> getCustomerProfile(String profileId) {
        return customerProfileRepository.getCustomerProfile(profileId).map(CustomerProfileEntity::toDomain);
    }

    public Optional<CustomerProfile> updateCustomerProfile(UpdateCustomerProfile updateCustomerProfile) {
        Optional<CustomerProfile> existedCustomer = getCustomerProfile(updateCustomerProfile.getId());
        if (existedCustomer.isEmpty()) {
            return Optional.empty();
        } else {
            CustomerProfile customerProfile = existedCustomer.get();
            customerProfile = customerProfile.toBuilder()
                                             .name(updateCustomerProfile.getName())
                                             .email(updateCustomerProfile.getEmail())
                                             .phone(updateCustomerProfile.getPhone())
                                             .longitude(updateCustomerProfile.getLongitude())
                                             .latitude(updateCustomerProfile.getLatitude())
                                             .build();
            return customerProfileRepository.updateCustomerProfile(CustomerProfileEntity.fromDomain(customerProfile))
                                            .map(CustomerProfileEntity::toDomain);
        }
    }

    public Optional<CustomerProfile> deleteCustomerProfile(String customerProfileId) {
        Optional<CustomerProfile> existedCustomer = getCustomerProfile(customerProfileId);
        if (existedCustomer.isEmpty()) {
            return Optional.empty();
        } else {
            return customerProfileRepository.deleteCustomerProfile(existedCustomer.map(CustomerProfileEntity::fromDomain)
                                                                                  .get())
                                            .map(CustomerProfileEntity::toDomain);
        }
    }
}
