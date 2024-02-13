package infrastructure;

import domain.listing.ListingDetails;
import domain.listing.ListingService;
import infrastructure.dto.ApiResponse;
import infrastructure.dto.in.listing.CreateListingDto;
import infrastructure.dto.in.listing.UpdateListingDto;
import infrastructure.dto.out.listing.ListingDetailsDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
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
        ListingDetails createdListing = listingService.createListing(createListingDto.toDomain());
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.empty(),
                                                                    200,
                                                                    Optional.of(ListingDetailsDto.fromDomain(createdListing)));
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
            //throw new BadRequestException("Cannot find listing with ID:" + listingId);
        }

        return Response.ok(response).build();
    }

    @POST
    @Path("/update-listing")
    public Response updateListing(UpdateListingDto updateListingDto) {
        Optional<ListingDetails> updatedListing = listingService.updateListing(updateListingDto.toDomain());
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.empty(),
                                                                  200,
                                                                  Optional.empty());
        if (updatedListing.isPresent()) {
            response.setData(Optional.of(ListingDetailsDto.fromDomain(updatedListing.get())));
        } else {
            response.setMessage(Optional.of("Cannot update listing with id " + updateListingDto.id() + " as listing was not found with given ID."));
            response.setCode(4001);
            //throw new BadRequestException("Cannot update listing with ID: " + updateListingDto.id() + ", as listing was not found with given ID.");
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
            //throw new BadRequestException("Cannot delete listing with ID: " + listingId + ", as listing was not found with given ID.");
        }
        return Response.ok(response).build();
    }
}
