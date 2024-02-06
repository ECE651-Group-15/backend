package infrastructure.dto.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import domain.listing.ListingDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingDetailsDto {
    public final String id;
    public final String title;
    public final String description;
    public final Optional<Double> price;
    public final Double longitude;
    public final Double latitude;
    public final String category;
    public final String userId;
    public final String status;
    public final List<String> images;
    public final Integer starCount;
    public final Instant createdAt;
    public final Instant updatedAt;

    public static ListingDetailsDto fromDomain(ListingDetails listingDetails) {
        return ListingDetailsDto.builder()
                                .id(listingDetails.getId())
                                .title(listingDetails.getTitle())
                                .description(listingDetails.getDescription())
                                .price(listingDetails.getPrice())
                                .longitude(listingDetails.getLongitude())
                                .latitude(listingDetails.getLatitude())
                                .category(String.valueOf(listingDetails.getCategory()))
                                .userId(listingDetails.getUserId())
                                .status(String.valueOf(listingDetails.getStatus()))
                                .images(listingDetails.getImages())
                                .starCount(listingDetails.getStarCount())
                                .createdAt(Instant.ofEpochMilli(listingDetails.getCreatedAt()))
                                .updatedAt(Instant.ofEpochMilli(listingDetails.getUpdatedAt()))
                                .build();
    }
}
