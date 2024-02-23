package domain.profile;

import domain.MD5Util;
import domain.listing.ListingRepository;
import domain.listing.StarListing;
import infrastructure.result.CustomerStarListingResult;
import infrastructure.result.DeleteCustomerResult;
import infrastructure.result.UpdateCustomerProfileResult;
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
		validateProfile(createCustomerProfile);
		Optional<CustomerProfile> existedCustomer = customerProfileRepository.getCustomerProfileByEmail(createCustomerProfile.getEmail())
																			 .map(CustomerProfileEntity::toDomain);
		if (existedCustomer.isPresent()) {
			return Optional.empty();
		}
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
				|| createCustomerProfile.getName() == null
				|| createCustomerProfile.getName().trim().isEmpty()
				|| createCustomerProfile.getPassword() == null
				|| createCustomerProfile.getPassword().trim().isEmpty()) {
			throw new BadRequestException("Email name, and password are required for a profile");
		}
	}

	public Optional<CustomerProfile> getCustomerProfile(String profileId) {
		return customerProfileRepository.getCustomerProfile(profileId).map(CustomerProfileEntity::toDomain);
	}

	@Transactional
	public UpdateCustomerProfileResult updateCustomerProfile(UpdateCustomerProfile updateCustomerProfile) {
		UpdateCustomerProfileResult updateCustomerProfileResult =
				UpdateCustomerProfileResult.builder()
										   .customerNotFound(false)
										   .validationError(false)
										   .updatedCustomerProfile(Optional.empty())
										   .build();
		Optional<CustomerProfileEntity> customerProfileEntity =
				requireUser(updateCustomerProfile.getId(),
							updateCustomerProfile.getPassword());
		if (customerProfileEntity.isEmpty()) {
			updateCustomerProfileResult.setValidationError(true);
			return updateCustomerProfileResult;
		}
		Optional<CustomerProfile> customerProfile =
				customerProfileRepository.updateCustomerProfile(updateCustomerProfile)
										 .map(CustomerProfileEntity::toDomain);
		if (customerProfile.isEmpty()) {
			updateCustomerProfileResult.setCustomerNotFound(true);
		}
		updateCustomerProfileResult.setUpdatedCustomerProfile(customerProfile);
		return updateCustomerProfileResult;
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
	public CustomerStarListingResult starListing(StarListing starListing) {
		CustomerStarListingResult customerStarListingResult =
				CustomerStarListingResult.builder()
										 .customerNotFound(false)
										 .listingNotFound(false)
										 .customerProfile(Optional.empty())
										 .build();
		Optional<ListingEntity> listingEntityOptional = listingRepository.getListing(starListing.getListingId());
		Optional<CustomerProfileEntity> customerProfileEntityOptional = customerProfileRepository.getCustomerProfile(
				starListing.getCustomerId());

		if (customerProfileEntityOptional.isEmpty()) {
			customerStarListingResult.setCustomerNotFound(true);
			return customerStarListingResult;
		}
		if (listingEntityOptional.isEmpty()) {
			customerStarListingResult.setListingNotFound(true);
			return customerStarListingResult;
		}
		ListingEntity listingEntity = listingEntityOptional.get();
		CustomerProfileEntity customerProfileEntity = customerProfileEntityOptional.get();

		List<ListingEntity> starredListings = customerProfileEntity.getStarredListings();
		if (!starredListings.contains(listingEntity)) {
			starredListings.add(listingEntity);
			customerProfileEntity.setStarredListings(starredListings);
		}
		Optional<CustomerProfile> fetchedCustomer =
				customerProfileRepository.getCustomerProfile(starListing.getCustomerId())
										 .map(CustomerProfileEntity::toDomain);
		customerStarListingResult.setCustomerProfile(fetchedCustomer);
		return customerStarListingResult;
	}

	public Optional<CustomerProfileEntity> requireUser(String id, String password) {
		Optional<CustomerProfile> customerProfile =
				customerProfileRepository.getCustomerProfile(id)
										 .map(CustomerProfileEntity::toDomain);
		return customerProfile.filter(profile -> profile.getPassword().equals(password))
							  .map(CustomerProfileEntity::fromDomain);
	}

	public List<CustomerProfile> getCustomerProfileByPage(int page,
														  int pageSize) {
		return customerProfileRepository.getCustomerProfileByPage(page, pageSize)
										.stream()
										.map(CustomerProfileEntity::toDomain)
										.toList();
	}
}
