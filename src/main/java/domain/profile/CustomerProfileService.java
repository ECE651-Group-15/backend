package domain.profile;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerProfileService {
    @Inject
    CustomerProfileRepository customerProfileRepository;

    public CustomerProfile createProfile(CreateCustomerProfile createCustomerProfile) {
        CustomerProfile customerProfile = CustomerProfile.builder()
                                                         .id(UUID.randomUUID().toString())
                                                         .name(createCustomerProfile.getName())
                                                         .email(createCustomerProfile.getEmail())
                                                         .phone(createCustomerProfile.getPhone())
                                                         .longitude(createCustomerProfile.getLongitude())
                                                         .latitude(createCustomerProfile.getLatitude())
                                                         .build();
        customerProfileRepository.save(customerProfile);
        return customerProfile;
    }

    public Optional<CustomerProfile> getCustomerProfile(String profileId) {
        return customerProfileRepository.getCustomerProfile(profileId);
    }

    public Optional<CustomerProfile> updateCustomerProfile(UpdateCustomerProfile updateCustomerProfile) {
        Optional<CustomerProfile> existedCustomer = getCustomerProfile(updateCustomerProfile.getId());
        if (existedCustomer.isEmpty()) {
            return Optional.empty();
        }
        else {
            CustomerProfile customerProfile = existedCustomer.get();
            customerProfile = customerProfile.toBuilder()
                                             .name(updateCustomerProfile.getName())
                                             .email(updateCustomerProfile.getEmail())
                                             .phone(updateCustomerProfile.getPhone())
                                             .longitude(updateCustomerProfile.getLongitude())
                                             .latitude(updateCustomerProfile.getLatitude())
                                             .build();
            return customerProfileRepository.updateCustomerProfile(customerProfile);
        }
    }

    public Optional<CustomerProfile> deleteCustomerProfile(String customerProfileId) {
        Optional<CustomerProfile> existedCustomer = getCustomerProfile(customerProfileId);
        if (existedCustomer.isEmpty()) {
            return Optional.empty();
        }
        else {
            return customerProfileRepository.deleteCustomerProfile(existedCustomer.get());
        }
    }
}
