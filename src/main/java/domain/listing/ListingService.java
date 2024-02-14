package domain.listing;

import domain.profile.CustomerProfileRepository;
import infrastructure.sql.entity.CustomerProfileEntity;
import infrastructure.sql.entity.ListingEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ListingService {

    @Inject
    ListingRepository listingRepository;

    @Inject
    CustomerProfileRepository customerProfileRepository;

    public Optional<ListingDetails> createListing(CreateListing createListing) {
        validateListing(createListing);

        Optional<CustomerProfileEntity> customerProfileEntity =
                customerProfileRepository.getCustomerProfile(createListing.getUserId());
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
                                                      .userId(createListing.getUserId())
                                                      .status(createListing.getStatus())
                                                      .images(createListing.getImages())
                                                      .createdAt(createListing.getCreatedAt())
                                                      .updatedAt(createListing.getUpdatedAt())
                                                      .build();

        ListingEntity listingEntity = ListingEntity.fromDomain(listingDetails,
                                                               customerProfileEntity.get());
        listingRepository.save(listingEntity);
        return Optional.of(listingDetails);
    }

    private void validateListing(CreateListing createListing) {
        if (createListing.getUserId() == null || createListing.getUserId().trim().isEmpty()) {
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

    public Optional<ListingDetails> updateListing(UpdateListing updateListing) {
        Optional<CustomerProfileEntity> customerProfileEntity =
                customerProfileRepository.getCustomerProfile(updateListing.getUserId());
        if (customerProfileEntity.isEmpty()) {
            return Optional.empty();
        }

        Optional<ListingDetails> existingListing = getListing(updateListing.getId());
        if (existingListing.isEmpty()) {
            return Optional.empty();
        }
        else {
            ListingDetails listingDetails = existingListing.get();
            listingDetails = listingDetails.toBuilder()
                                           .title(updateListing.getTitle())
                                           .description(updateListing.getDescription())
                                           .price(updateListing.getPrice())
                                           .latitude(updateListing.getLatitude())
                                           .longitude(updateListing.getLongitude())
                                           .category(updateListing.getCategory())
                                           .status(updateListing.getStatus())
                                           .images(updateListing.getImages())
                                           .updatedAt(Instant.now().toEpochMilli())
                                           .build();
            ListingEntity listingEntity = ListingEntity.fromDomain(listingDetails,
                                                                   customerProfileEntity.get());
            return listingRepository.updateListing(listingEntity).map(ListingEntity::toDomain);
        }
    }

    public Optional<ListingDetails> deleteListing(String listingId) {
        Optional<ListingDetails> existingListing = getListing(listingId);
        if (existingListing.isEmpty()) {
            return Optional.empty();
        }
        else {
            Optional<CustomerProfileEntity> customerProfileEntity =
                    customerProfileRepository.getCustomerProfile(existingListing.get().userId);
            if (customerProfileEntity.isEmpty()) {
                return Optional.empty();
            }
            ListingEntity listingEntity = ListingEntity.fromDomain(existingListing.get(),
                                                                   customerProfileEntity.get());
            return listingRepository.deleteListing(listingEntity).map(ListingEntity::toDomain);
        }
    }
}
