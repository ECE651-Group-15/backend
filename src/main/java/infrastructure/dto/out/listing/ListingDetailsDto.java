package infrastructure.dto.out.listing;

import com.fasterxml.jackson.annotation.JsonInclude;
import domain.listing.ListingDetails;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ListingDetailsDto(String id,
                                String title,
                                String description,
                                Optional<Double> price,
                                Double longitude,
                                Double latitude,
                                String category,
                                String userId,
                                String status,
                                List<String> images,
                                Instant createdAt,
                                Instant updatedAt) {
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
                                .createdAt(Instant.ofEpochMilli(listingDetails.getCreatedAt()))
                                .updatedAt(Instant.ofEpochMilli(listingDetails.getUpdatedAt()))
                                .build();
    }
}
