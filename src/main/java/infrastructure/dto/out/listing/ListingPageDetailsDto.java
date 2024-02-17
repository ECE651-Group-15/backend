package infrastructure.dto.out.listing;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ListingPageDetailsDto(List<ListingWithCustomerInfoDto> listingDetails) {
}
