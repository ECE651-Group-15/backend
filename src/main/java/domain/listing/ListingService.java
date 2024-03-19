package domain.listing;

import domain.profile.CustomerProfileRepository;
import infrastructure.result.ListingStarResult;
import infrastructure.result.ListingUnStarResult;
import infrastructure.result.UpdateListingResult;
import infrastructure.sql.entity.CustomerProfileEntity;
import infrastructure.sql.entity.ListingEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ListingService {
    private final Integer DEFAULT_PAGE_SIZE = 20;

    @Inject
    ListingRepository listingRepository;

    @Inject
    CustomerProfileRepository customerProfileRepository;

    @Transactional
    public Optional<ListingDetails> createListing(CreateListing createListing) {
        validateListing(createListing);

        Optional<CustomerProfileEntity> customerProfileEntity =
                customerProfileRepository.getCustomerProfile(createListing.getCustomerId());
        if (customerProfileEntity.isEmpty()) {
            return Optional.empty();
        }

        ListingDetails listingDetails = ListingDetails.builder()
                                                      .id(UUID.randomUUID().toString())
                                                      .title(createListing.getTitle())
                                                      .description(createListing.getDescription())
                                                      .price(createListing.getPrice())
                                                      .latitude(createListing.getLatitude())
                                                      .longitude(createListing.getLongitude())
                                                      .category(createListing.getCategory())
                                                      .customerId(createListing.getCustomerId())
                                                      .status(createListing.getStatus())
                                                      .images(createListing.getImages())
                                                      .createdAt(createListing.getCreatedAt())
                                                      .updatedAt(createListing.getUpdatedAt())
                                                      .build();

        ListingEntity listingEntity = ListingEntity.fromDomain(listingDetails,
                                                               customerProfileEntity.get());

        List<ListingEntity> postedListings = customerProfileEntity.get().getPostedListings();
        postedListings.add(listingEntity);
        customerProfileEntity.get().setPostedListings(postedListings);
        listingRepository.save(listingEntity);
        return Optional.of(listingDetails);
    }

    public void validateListing(CreateListing createListing) {
        if (createListing.getCustomerId() == null || createListing.getCustomerId().trim().isEmpty()) {

            throw new BadRequestException("User ID is required");

        } else if (createListing.getTitle() == null || createListing.getTitle().trim().isEmpty()) {

            throw new BadRequestException("Title is required");
        } else if (createListing.getDescription() == null || createListing.getDescription().trim().isEmpty()) {

            throw new BadRequestException("Description is required");
        }

    }

    public Optional<ListingDetails> getListing(String listingId) {
        return listingRepository.getListing(listingId).map(ListingEntity::toDomain);
    }

    @Transactional
    public UpdateListingResult updateListing(UpdateListing updateListing) {
        // TODO: add check customerId here, we shouldn't allow customer to update other customer's listing
        UpdateListingResult updateResult = UpdateListingResult.builder()
                                                              .customerNotFound(false)
                                                              .listingNotFound(false)
                                                              .updatedListing(Optional.empty())
                                                              .build();
        Optional<CustomerProfileEntity> customerProfileEntity =
                customerProfileRepository.getCustomerProfile(updateListing.getCustomerId());
        if (customerProfileEntity.isEmpty()) {
            updateResult.setCustomerNotFound(true);
        }
        Optional<ListingEntity> listingEntityOptional = listingRepository.updateListing(updateListing);
        if (listingEntityOptional.isEmpty()) {
            updateResult.setListingNotFound(true);
        } else {
            updateResult.setUpdatedListing(listingEntityOptional.map(ListingEntity::toDomain));
        }
        return updateResult;
    }

    @Transactional
    public Optional<ListingDetails> deleteListing(String listingId) {
        Optional<ListingDetails> existingListing = getListing(listingId);

        if (existingListing.isEmpty()) {
            return Optional.empty();
        } else {
            Optional<CustomerProfileEntity> customerProfileEntity =
                    customerProfileRepository.getCustomerProfile(existingListing.get().customerId);
            if (customerProfileEntity.isEmpty()) {
                return Optional.empty();
            }
            List<ListingEntity> postedListings = customerProfileEntity.get().getPostedListings();
            postedListings.removeIf(listingEntity -> listingEntity.getId().equals(listingId));
            customerProfileEntity.get().setPostedListings(postedListings);
            List<ListingEntity> starredListings = customerProfileEntity.get().getStarredListings();
            starredListings.removeIf(listingEntity -> listingEntity.getId().equals(listingId));
            customerProfileEntity.get().setStarredListings(starredListings);

            ListingEntity listingEntity = ListingEntity.fromDomain(existingListing.get(),
                                                                   customerProfileEntity.get());
            return listingRepository.deleteListing(listingEntity).map(ListingEntity::toDomain);
        }
    }


    @Transactional
    public ListingStarResult starListing(StarListing starListing) {
		ListingStarResult listingStarResult = ListingStarResult.builder()
															   .listingNotFound(false)
															   .customerNotFound(false)
															   .listingDetails(Optional.empty())
															   .build();
        Optional<ListingEntity> listingEntityOptional = listingRepository.getListing(starListing.getListingId());
        Optional<CustomerProfileEntity> customerProfileEntityOptional = customerProfileRepository.getCustomerProfile(
                starListing.getCustomerId());

        if (customerProfileEntityOptional.isEmpty()) {
			listingStarResult.setCustomerNotFound(true);
			return listingStarResult;
		}
		else if (listingEntityOptional.isEmpty()) {
			listingStarResult.setListingNotFound(true);
			return listingStarResult;
		}
        ListingEntity listingEntity = listingEntityOptional.get();
        CustomerProfileEntity customerProfileEntity = customerProfileEntityOptional.get();

        List<CustomerProfileEntity> customersWhoStarred = listingEntity.getCustomersWhoStarred();
        if (!customersWhoStarred.contains(customerProfileEntity)) {
            customersWhoStarred.add(customerProfileEntity);
            listingEntity.setCustomersWhoStarred(customersWhoStarred);
        }

        Optional<ListingDetails> listingDetails = listingRepository.getListing(starListing.getListingId())
																   .map(ListingEntity::toDomain);
		return listingStarResult.toBuilder()
								.listingDetails(listingDetails)
								.build();
    }

	@Transactional
	public ListingUnStarResult unStarListing(StarListing starListing) {
		ListingUnStarResult listingUnStarResult = ListingUnStarResult.builder()
																	 .listingNotFound(false)
																	 .customerNotFound(false)
																	 .listingDetails(Optional.empty())
																	 .build();
		Optional<ListingEntity> listingEntityOptional = listingRepository.getListing(starListing.getListingId());
		Optional<CustomerProfileEntity> customerProfileEntityOptional =
				customerProfileRepository.getCustomerProfile(starListing.getCustomerId());

		if (customerProfileEntityOptional.isEmpty()) {
			listingUnStarResult.setCustomerNotFound(true);
			return listingUnStarResult;
		}
		else if (listingEntityOptional.isEmpty()) {
			listingUnStarResult.setListingNotFound(true);
			return listingUnStarResult;
		}
		ListingEntity listingEntity = listingEntityOptional.get();
		CustomerProfileEntity customerProfileEntity = customerProfileEntityOptional.get();

		List<CustomerProfileEntity> customersWhoStarred = listingEntity.getCustomersWhoStarred();
		if (customersWhoStarred.contains(customerProfileEntity)) {
			customersWhoStarred.remove(customerProfileEntity);
			listingEntity.setCustomersWhoStarred(customersWhoStarred);
		}

		Optional<ListingDetails> listingDetails = listingRepository.getListing(starListing.getListingId())
																   .map(ListingEntity::toDomain);
		return listingUnStarResult.toBuilder()
								  .listingDetails(listingDetails)
								  .build();
	}

    public List<ListingDetails> getListingPage(int page, Optional<Integer> pageSize, Optional<List<String>> listingIds) {
        if (listingIds.isEmpty()) {
            return List.of();
        } else {
            if (listingIds.get().size() < (page * pageSize.orElse(DEFAULT_PAGE_SIZE))) {
                return List.of();
            }
            List<ListingDetails> listings = new ArrayList<>();
            for (int i = page * pageSize.orElse(DEFAULT_PAGE_SIZE); i < (page + 1) * pageSize.orElse(DEFAULT_PAGE_SIZE); i++) {
                if (i >= listingIds.get().size()) {
                    break;
                }
                String listingId = listingIds.get().get(i);
                Optional<ListingDetails> listing = listingRepository.getListing(listingId)
																	.map(ListingEntity::toDomain);
                listing.ifPresent(listings::add);
            }
            listings.sort((l1, l2) -> Long.compare(l2.getUpdatedAt(), l1.getUpdatedAt()));
            return listings;
        }
    }

    public List<ListingWithCustomerInfo> getListingAndCustomerByPage(int page, int pageSize) {
        return listingRepository.getListingPage(page, pageSize)
                                .stream()
								.filter(listingEntity -> listingEntity.getStatus().equals(ListingStatus.ACTIVE))
								.map(listingEntity -> {
                                    Optional<CustomerProfileEntity> customerProfileEntity =
                                            customerProfileRepository.getCustomerProfile(listingEntity.getCustomerProfile().getId());
                                    return customerProfileEntity.map(customerProfile -> ListingWithCustomerInfo.builder()
                                                                                                               .listingDetails(
                                                                                                                       listingEntity.toDomain())
                                                                                                               .customerProfile(
                                                                                                                       customerProfile.toDomain())
                                                                                                               .build());
                                })
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .toList();
    }

	public List<ListingDetails> searchListing(SearchListing searchListing) {
		return listingRepository.getListingByTitle(searchListing.getTitle())
								.stream()
								.filter(listingEntity -> listingEntity.getStatus().equals(ListingStatus.ACTIVE))
								.map(ListingEntity::toDomain)
								.toList();
	}
}
