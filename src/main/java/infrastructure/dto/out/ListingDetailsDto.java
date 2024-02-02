package infrastructure.dto.out;

import domain.listing.ListingDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Builder
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
    public final String createdAt;
    public final String updatedAt;

    public static ListingDetailsDto fromDomain(ListingDetails listingDetails) {
        return ListingDetailsDto.builder()
                                .id(listingDetails.getId())
                                .title(listingDetails.getTitle())
                                .description(listingDetails.getDescription())
                                .price(listingDetails.getPrice())
                                .latitude(listingDetails.getPosition().getLatitude())
                                .latitude(listingDetails.getPosition().getLongitude())
                                .category(String.valueOf(listingDetails.getCategory()))
                                .userId(listingDetails.getUserId())
                                .status(String.valueOf(listingDetails.getStatus()))
                                .images(listingDetails.getImages().stream()
                                                      .map(ListingDetails::encodeImage)
                                                      .toList())
                                .starCount(listingDetails.getStarCount())
                                .createdAt(listingDetails.getCreatedAt())
                                .updatedAt(listingDetails.getUpdatedAt())
                                .build();
    }
}
