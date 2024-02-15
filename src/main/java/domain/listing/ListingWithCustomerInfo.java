package domain.listing;

import domain.profile.CustomerProfile;
import lombok.Builder;

@Builder
public record ListingWithCustomerInfo (ListingDetails listingDetails,
                                       CustomerProfile customerProfile){
}
