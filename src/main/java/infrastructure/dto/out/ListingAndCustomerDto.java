package infrastructure.dto.out;

import domain.listing.ListingDetails;
import domain.profile.CustomerProfile;
import infrastructure.dto.out.listing.ListingDetailsDto;
import infrastructure.dto.out.profile.CustomerProfilesDetailsDto;
import lombok.Builder;

@Builder
public record ListingAndCustomerDto(ListingDetailsDto listingDetails,
                                    CustomerProfilesDetailsDto customerProfilesDetails) {
    public static ListingAndCustomerDto fromDomain(ListingDetails listingDetails,
                                                   CustomerProfile customerProfile) {
        return ListingAndCustomerDto.builder()
                                    .listingDetails(ListingDetailsDto.fromDomain(listingDetails))
                                    .customerProfilesDetails(CustomerProfilesDetailsDto.fromDomain(customerProfile))
                                    .build();
    }
}
