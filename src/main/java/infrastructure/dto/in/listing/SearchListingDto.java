package infrastructure.dto.in.listing;

import domain.listing.SearchListing;

public record SearchListingDto(String title) {
	public SearchListing toDomain() {
		return SearchListing.builder()
		                    .title(title)
		                    .build();
	}
}
