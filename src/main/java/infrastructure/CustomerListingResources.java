package infrastructure;

import domain.listing.ListingDetails;
import domain.listing.ListingService;
import domain.profile.CustomerProfile;
import domain.profile.CustomerProfileService;
import infrastructure.dto.ApiResponse;
import infrastructure.dto.in.listing.ListingPageDto;
import infrastructure.dto.in.listing.StarListingDto;
import infrastructure.dto.in.profile.GetCustomerPostedListingsDto;
import infrastructure.dto.out.listing.ListingWithCustomerInfoDto;
import infrastructure.dto.out.listing.ListingDetailsDto;
import infrastructure.dto.out.listing.ListingPageDetailsDto;
import infrastructure.dto.out.listing.PostedListingPageDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("v1/api/listing-profile")
@Produces("application/json")
public class CustomerListingResources {
    @Inject
    CustomerProfileService customerProfileService;

    @Inject
    ListingService listingService;

    @POST
    @Path("/star-listing")
    public Response starListingForCustomer(StarListingDto starListingDto) {
        ApiResponse<ListingWithCustomerInfoDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        Optional<ListingDetails> listingDetails = listingService.starListing(starListingDto.toDomain());
        Optional<CustomerProfile> customerProfile = customerProfileService.starListing(starListingDto.toDomain());
        if (customerProfile.isEmpty()) {
            response.setMessage(Optional.of("Cannot find customer with id " + starListingDto.customerId() + "."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
        if (listingDetails.isEmpty()) {
            response.setMessage(Optional.of("Cannot find listing with id " + starListingDto.listingId() + "."));
            response.setCode(4001);
            return Response.ok(response).build();
        }

        response.setData(Optional.of(ListingWithCustomerInfoDto.fromDomain(listingDetails.get(), customerProfile.get())));
        return Response.ok(response).build();
    }

    @POST
    @Path("/get-customer-posted-listings")
    public Response getCustomerPostedListings(GetCustomerPostedListingsDto getCustomerListingsDto) {
<<<<<<< HEAD:src/main/java/infrastructure/CustomerListingResources.java
        Optional<CustomerProfile> fetchedCustomerProfile = customerProfileService.getCustomerProfile(getCustomerListingsDto.customerId());
=======
        Optional<CustomerProfile> fetchedCustomerProfile =
                customerProfileService.getCustomerProfile(getCustomerListingsDto.customerId());
>>>>>>> main:src/main/java/infrastructure/ListingAndCustomerResources.java

        ApiResponse<PostedListingPageDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        if (getCustomerListingsDto.page() < 0) {
            response.setMessage(Optional.of("Page number cannot be negative."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
        if (fetchedCustomerProfile.isPresent()) {
            List<ListingDetails> postedListings = listingService.getListingPage(getCustomerListingsDto.page(),
                                                                                getCustomerListingsDto.pageSize(),
                                                                                fetchedCustomerProfile.get().getPostedListingIds());
            response.setData(Optional.of(PostedListingPageDto.builder()
                                                             .postedListings(
                                                                     postedListings.stream().map(ListingDetailsDto::fromDomain).toList())
                                                             .build()));
        } else {
            response.setMessage(Optional.of("Cannot find customer with id " + getCustomerListingsDto.customerId() + "."));
            response.setCode(4001);
        }

        return Response.ok(response).build();
    }

    @POST
    @Path("/get-listing-page")
    public Response getListingByPage(ListingPageDto listingPageDto) {
        ApiResponse<ListingPageDetailsDto> response = new ApiResponse<>(Optional.empty(), 200, Optional.empty());

        if (listingPageDto.page < 0 || (listingPageDto.pageSize.isPresent() && listingPageDto.pageSize.get() < 0)) {
            response.setMessage(Optional.of("Page number and page size cannot be negative."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
        List<ListingWithCustomerInfoDto> listingAndCustomerDetails
                = listingService.getListingAndCustomerByPage(listingPageDto.page,
                                                             listingPageDto.pageSize.orElse(20))
                                .stream()
                                .map(ListingWithCustomerInfoDto::fromDomain)
                                .toList();
        if (listingAndCustomerDetails.isEmpty()) {
            response.setMessage(Optional.of("No listings found."));
        }
        response.setData(Optional.of(ListingPageDetailsDto.builder().listingDetails(listingAndCustomerDetails).build()));
        return Response.ok(response).build();
    }
}
