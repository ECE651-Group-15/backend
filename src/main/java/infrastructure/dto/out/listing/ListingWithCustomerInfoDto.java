package infrastructure.dto.out.listing;

import domain.listing.ListingDetails;
import domain.listing.ListingWithCustomerInfo;
import domain.profile.CustomerProfile;
import infrastructure.dto.out.profile.CustomerProfilesDetailsDto;
import lombok.Builder;

@Builder
public record ListingWithCustomerInfoDto(ListingDetailsDto listingDetails,
                                         CustomerProfilesDetailsDto customerProfilesDetails) {
    public static ListingWithCustomerInfoDto fromDomain(ListingDetails listingDetails,
                                                        CustomerProfile customerProfile) {
        return ListingWithCustomerInfoDto.builder()
                                         .listingDetails(ListingDetailsDto.fromDomain(listingDetails))
                                         .customerProfilesDetails(CustomerProfilesDetailsDto.fromDomain(customerProfile))
                                         .build();
    }

    public static ListingWithCustomerInfoDto fromDomain(ListingWithCustomerInfo listingWithCustomerInfo) {
        return ListingWithCustomerInfoDto.builder()
                                         .listingDetails(ListingDetailsDto.fromDomain(listingWithCustomerInfo.listingDetails()))
                                         .customerProfilesDetails(
                                                 CustomerProfilesDetailsDto.fromDomain(listingWithCustomerInfo.customerProfile()))
                                         .build();
    }
}
