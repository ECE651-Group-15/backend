package domain.listing;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StarListing {
    String customerId;
    String listingId;
}
