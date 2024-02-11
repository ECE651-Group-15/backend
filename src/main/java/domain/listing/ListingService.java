package domain.listing;

import infrastructure.sql.ListingRepository;
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

    public ListingDetails createListing(CreateListing createListing) {
        if (createListing.getUserId() == null || createListing.getUserId().trim().isEmpty()) {
            throw new BadRequestException("User ID is required");
        }
        else if (createListing.getTitle() == null || createListing.getTitle().trim().isEmpty()){
            throw new BadRequestException("Title is required");

        }
        else if (createListing.getDescription() == null || createListing.getDescription().trim().isEmpty()){
            throw new BadRequestException("Description is required");
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
                                                      .starCount(0)
                                                      .createdAt(createListing.getCreatedAt())
                                                      .updatedAt(createListing.getUpdatedAt())
                                                      .build();
        listingRepository.save(listingDetails);
        return listingDetails;
    }

    public Optional<ListingDetails> getListing(String listingId) {
        return listingRepository.getListing(listingId).map(ListingEntity::toDomain);
    }

    public Optional<ListingDetails> updateListing(UpdateListing updateListing) {
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
                                           .starCount(updateListing.getStarCount())
                                           .updatedAt(Instant.now().toEpochMilli())
                                           .build();
            return listingRepository.updateListing(listingDetails);
        }
    }

    public Optional<ListingDetails> deleteListing(String listingId) {
        Optional<ListingDetails> existingListing = getListing(listingId);
        if (existingListing.isEmpty()) {
            return Optional.empty();
        }
        else {
            return listingRepository.delete(existingListing.get());
        }
    }



    public ListingDetails[] getListings() {
        return null;
    }
}
