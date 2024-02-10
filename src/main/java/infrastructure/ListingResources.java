package infrastructure;

import domain.listing.ListingService;
import infrastructure.dto.in.CreateListingDto;
import infrastructure.dto.in.UpdateListingDto;
import infrastructure.dto.out.ListingDetailsDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@ApplicationScoped
@Path("v1/api/listings")
@Produces("application/json")
public class ListingResources {

    @Inject
    ListingService listingService;

    @POST
    @Path("/create-listing")
    public ListingDetailsDto createListing(CreateListingDto createListingDto) {
        return ListingDetailsDto.fromDomain(listingService.createListing(createListingDto.toDomain()));
    }

    @POST
    @Path("/get-listing/{listingId}")
    public ListingDetailsDto getListing(@PathParam("listingId") String listingId) {
        return ListingDetailsDto.fromDomain(listingService.getListing(listingId));
    }

    @POST
    @Path("/update-listing")
    public ListingDetailsDto updateListing(UpdateListingDto updateListingDto) {
        return ListingDetailsDto.fromDomain(listingService.updateListing(updateListingDto.toDomain()));
    }
}
