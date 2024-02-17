package infrastructure.dto.out.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@Builder
public class StarredListingPageDto {
    public final List<ListingDetailsDto> starredListings;
}
