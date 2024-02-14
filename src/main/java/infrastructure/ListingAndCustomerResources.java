package infrastructure;

import domain.listing.ListingDetails;
import domain.listing.ListingService;
import domain.profile.CustomerProfile;
import domain.profile.CustomerProfileService;
import infrastructure.dto.ApiResponse;
import infrastructure.dto.in.listing.StarListingDto;
import infrastructure.dto.out.ListingAndCustomerDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@ApplicationScoped
@Path("v1/api/listing-profile")
@Produces("application/json")
public class ListingAndCustomerResources {
    @Inject
    CustomerProfileService customerProfileService;

    @Inject
    ListingService listingService;

    @POST
    @Path("/star-listing")
    public Response starListingForCustomer(StarListingDto starListingDto) {
        ApiResponse<ListingAndCustomerDto> response = new ApiResponse<>(Optional.empty(),
                                                                        200,
                                                                        Optional.empty());

        Optional<ListingDetails> listingDetails = listingService.starListing(starListingDto.toDomain());
        Optional <CustomerProfile> customerProfile = customerProfileService.starListing(starListingDto.toDomain());
        if (customerProfile.isEmpty()){
            response.setMessage(Optional.of("Cannot find customer with id " + starListingDto.customerId() + "."));
            response.setCode(4001);
            return Response.ok(response).build();
        }
        if (listingDetails.isEmpty()){
            response.setMessage(Optional.of("Cannot find listing with id " + starListingDto.listingId() + "."));
            response.setCode(4001);
            return Response.ok(response).build();
        }

        response.setData(Optional.of(ListingAndCustomerDto.fromDomain(listingDetails.get(), customerProfile.get())));
        return Response.ok(response).build();
    }
}
