package infrastructure;

import domain.listing.ListingDetails;
import domain.listing.ListingService;
import infrastructure.dto.ApiResponse;
import infrastructure.dto.in.listing.CreateListingDto;
import infrastructure.dto.in.listing.UpdateListingDto;
import infrastructure.dto.out.listing.ListingDetailsDto;
import infrastructure.result.UpdateListingResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@ApplicationScoped
@Path("v1/api/listings")
@Produces("application/json")
public class ListingResources {

    @Inject
    ListingService listingService;

    @POST
    @Path("/create-listing")
    public Response createListing(CreateListingDto createListingDto) {
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.empty(),
                                                                    200,
                                                                    Optional.empty());
        Optional<ListingDetails> createdListing = listingService.createListing(createListingDto.toDomain());
        if (createdListing.isEmpty()) {
            response.setMessage(Optional.of("Cannot find create listing with customer id: " + createListingDto.customerId() + "."));
            response.setCode(4001);
        } else {
            response.setData(Optional.of(ListingDetailsDto.fromDomain(createdListing.get())));
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/get-listing/{listingId}")
    public Response getListing(@PathParam("listingId") String listingId) {
        Optional<ListingDetails> fetchedListing = listingService.getListing(listingId);
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.empty(),
                                                                    200,
                                                                    Optional.empty());
        if (fetchedListing.isPresent()) {
            response.setData(Optional.of(ListingDetailsDto.fromDomain(fetchedListing.get())));
        } else {
            response.setMessage(Optional.of("Cannot find listing with id " + listingId + "."));
            response.setCode(4001);
        }

        return Response.ok(response).build();
    }

    @POST
    @Path("/update-listing")
    public Response updateListing(UpdateListingDto updateListingDto) {
        UpdateListingResult updateListingResult = listingService.updateListing(updateListingDto.toDomain());
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.empty(),
                                                                    200,
                                                                    Optional.empty());
        if (updateListingResult.getUpdatedListing().isPresent()) {
            response.setData(Optional.of(ListingDetailsDto.fromDomain(updateListingResult.getUpdatedListing().get())));
        } else if (updateListingResult.isCustomerNotFound()) {
            response.setMessage(Optional.of("Cannot update listing with id " + updateListingDto.id() + " as customer was not found with given ID."));
            response.setCode(4001);
        } else if (updateListingResult.isListingNotFound()) {
            response.setMessage(Optional.of("Cannot update listing with id " + updateListingDto.id() + " as listing was not found with given ID."));
            response.setCode(4001);
        }
        return Response.ok(response).build();
    }

    @POST
    @Path("/delete-listing/{listingId}")
    public Response deleteListing(@PathParam("listingId") String listingId) {
        Optional<ListingDetails> deletedListing = listingService.deleteListing(listingId);
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.empty(),
                                                                    200,
                                                                    Optional.empty());
        if (deletedListing.isPresent()) {
            response.setData(Optional.of(ListingDetailsDto.fromDomain(deletedListing.get())));
        } else {
            response.setMessage(Optional.of("Cannot delete listing with id " + listingId + " as listing was not found with given ID."));
            response.setCode(4001);
        }
        return Response.ok(response).build();
    }

}
