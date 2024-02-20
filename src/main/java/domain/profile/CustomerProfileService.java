package domain.profile;

import domain.MD5Util;
import domain.listing.ListingRepository;
import domain.listing.StarListing;
import infrastructure.result.DeleteCustomerResult;
import infrastructure.sql.entity.CustomerProfileEntity;
import infrastructure.sql.entity.ListingEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerProfileService {
    @Inject
    CustomerProfileRepository customerProfileRepository;

    @Inject
    ListingRepository listingRepository;

    public Optional<CustomerProfile> createProfile(CreateCustomerProfile createCustomerProfile) {
        Optional<CustomerProfile> existedCustomer = customerProfileRepository.getCustomerProfileByEmail(createCustomerProfile.getEmail())
                                                                             .map(CustomerProfileEntity::toDomain);
        if (existedCustomer.isPresent()) {
            return Optional.empty();
        }
        validateProfile(createCustomerProfile);
        CustomerProfile customerProfile = CustomerProfile.builder()
                                                         .id(UUID.randomUUID().toString())
                                                         .name(createCustomerProfile.getName())
                                                         .password(createCustomerProfile.getPassword())
                                                         .email(createCustomerProfile.getEmail())
                                                         .avatar(MD5Util.md5Hex(createCustomerProfile.getEmail()))
                                                         .phone(createCustomerProfile.getPhone())
                                                         .longitude(createCustomerProfile.getLongitude())
                                                         .latitude(createCustomerProfile.getLatitude())
                                                         .postedListingIds(Optional.of(List.of()))
                                                         .starredListingIds(Optional.of(List.of()))
                                                         .build();
        CustomerProfileEntity customerProfileEntity = CustomerProfileEntity.fromDomain(customerProfile);
        customerProfileRepository.save(customerProfileEntity);
        return Optional.of(customerProfile);
    }

    private void validateProfile(CreateCustomerProfile createCustomerProfile) {
        if (createCustomerProfile.getEmail() == null
                || createCustomerProfile.getEmail().trim().isEmpty()
                || createCustomerProfile.getName() == null || createCustomerProfile.getName().trim().isEmpty()) {
            throw new BadRequestException("Email and name are required for a profile");
        }
    }

    public Optional<CustomerProfile> getCustomerProfile(String profileId) {
        return customerProfileRepository.getCustomerProfile(profileId).map(CustomerProfileEntity::toDomain);
    }

    public Optional<CustomerProfile> updateCustomerProfile(UpdateCustomerProfile updateCustomerProfile) {
        boolean verified = verifyUser(updateCustomerProfile.getId(),
                                      updateCustomerProfile.getEmail(),
                                      updateCustomerProfile.getPassword());
        if (!verified) {
            return Optional.empty();
        }

        Optional<CustomerProfile> existedCustomer = getCustomerProfile(updateCustomerProfile.getId());
        if (existedCustomer.isEmpty()) {
            return Optional.empty();
        } else {
            CustomerProfile customerProfile = existedCustomer.get();
            customerProfile = customerProfile.toBuilder()
                                             .email(updateCustomerProfile.getEmail())
                                             .password(updateCustomerProfile.getPassword())
                                             .name(updateCustomerProfile.getName())
                                             .phone(updateCustomerProfile.getPhone())
                                             .longitude(updateCustomerProfile.getLongitude())
                                             .latitude(updateCustomerProfile.getLatitude())
                                             .build();
            return customerProfileRepository.updateCustomerProfile(CustomerProfileEntity.fromDomain(customerProfile))
                                            .map(CustomerProfileEntity::toDomain);
        }
    }

    public DeleteCustomerResult deleteCustomerProfile(String customerProfileId) {
        DeleteCustomerResult deleteCustomerResult = DeleteCustomerResult.builder()
                                                                        .hasCreatedListings(false)
                                                                        .deletedCustomerProfile(Optional.empty())
                                                                        .build();
        Optional<CustomerProfile> existedCustomer = getCustomerProfile(customerProfileId);
        if (existedCustomer.isPresent()) {
            deleteCustomerResult = deleteCustomerResult.toBuilder()
                                                       .deletedCustomerProfile(existedCustomer)
                                                       .build();
            if (existedCustomer.get().getPostedListingIds().isPresent()
                    && !existedCustomer.get().getPostedListingIds().get().isEmpty()) {
                deleteCustomerResult = deleteCustomerResult.toBuilder()
                                                           .hasCreatedListings(true)
                                                           .build();
                return deleteCustomerResult;
            }
            deleteCustomerResult.toBuilder()
                                .deletedCustomerProfile(customerProfileRepository.deleteCustomerProfile(
                                                                                         existedCustomer.map(CustomerProfileEntity::fromDomain)
                                                                                                        .get())
                                                                                 .map(CustomerProfileEntity::toDomain));
        }
        return deleteCustomerResult;
    }


    @Transactional
    public Optional<CustomerProfile> starListing(StarListing starListing) {
        Optional<ListingEntity> listingEntityOptional = listingRepository.getListing(starListing.getListingId());
        Optional<CustomerProfileEntity> customerProfileEntityOptional = customerProfileRepository.getCustomerProfile(
                starListing.getCustomerId());

        if (listingEntityOptional.isEmpty() || customerProfileEntityOptional.isEmpty()) {
            return Optional.empty();
        }
        ListingEntity listingEntity = listingEntityOptional.get();
        CustomerProfileEntity customerProfileEntity = customerProfileEntityOptional.get();

        List<ListingEntity> starredListings = customerProfileEntity.getStarredListings();
        if (!starredListings.contains(listingEntity)) {
            starredListings.add(listingEntity);
            customerProfileEntity.setStarredListings(starredListings);
            customerProfileRepository.updateCustomerProfile(customerProfileEntity);
        }
        return customerProfileRepository.getCustomerProfile(starListing.getCustomerId())
                                        .map(CustomerProfileEntity::toDomain);
    }

    public boolean verifyUser(String id, String email, String password) {
        Optional<CustomerProfile> customerProfile = customerProfileRepository.getCustomerProfile(id).map(CustomerProfileEntity::toDomain);
        return customerProfile.map(profile -> profile.getEmail().equals(email) && profile.getPassword().equals(password)).orElse(false);
    }

    public List<CustomerProfile> getCustomerProfileByPage(int page,
                                                          int pageSize) {
        return customerProfileRepository.getCustomerProfileByPage(page, pageSize)
                                        .stream()
                                        .map(CustomerProfileEntity::toDomain)
                                        .toList();
    }
}
