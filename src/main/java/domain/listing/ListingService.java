package domain.listing;

import domain.profile.CustomerProfileRepositoryInterface;
import infrastructure.sql.entity.ListingEntity;
import infrastructure.sql.entity.ProfileEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;


@ApplicationScoped
public class ListingService {

    @Inject
    ListingRepositoryInterface listingRepository;
    @Inject
    CustomerProfileRepositoryInterface customerProfileRepository;

    public ListingDetails createListing(CreateListing createListing) {
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

    //Get StarredListingIds ID to retrieve post details
    public ListingPage getStarredListingIds(String userId, int pageIndex, int pageSize) {
        // find ProfileEntity，if does not exist return exception
        ProfileEntity customerProfile = customerProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID not found"));

        // get page
        List<ListingDetails> starredListings = customerProfile.getStarredListingIds().stream()
                .skip((long) pageIndex * pageSize) // skip current page
                .limit(pageSize) // limit current page size
                .map(listingId -> listingRepository.getListing(listingId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ListingEntity::toDomain)
                .collect(Collectors.toList());

        // create ListingPage
        ListingPage listingPage = new ListingPage();
        listingPage.setListings(starredListings);
        listingPage.setPageIndex(pageIndex);
        listingPage.setPageSize(pageSize);

        return listingPage;
    }









    //Retrieve paginated query results of posts based on the incoming user and page number
    public ListingPage getStarredListings(String userId, int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        PanacheQuery<ListingEntity> listings = listingRepository.findByUserId(userId, page);
        List<ListingDetails> listingDetails = listings.list().stream()
                .map(ListingEntity::toDomain)
                .collect(Collectors.toList());

        ListingPage listingPage = new ListingPage();
        listingPage.setListings(listingDetails);
        listingPage.setPageIndex(pageIndex);
        listingPage.setPageSize(pageSize);
        listingPage.setTotalPages(listings.pageCount());

        return listingPage;
    }

}

