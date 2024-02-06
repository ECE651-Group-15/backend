package infrastructure;

import domain.listing.ListingService;
import infrastructure.dto.in.CreateListingDto;
import infrastructure.dto.out.ListingDetailsDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@ApplicationScoped
@Path("/api/listings")
@Produces("application/json")
public class ListingResources {

    @Inject
    ListingService listingService;

    @POST
    @Path("/create")
    public ListingDetailsDto createListing(CreateListingDto createListingDto) {
        return ListingDetailsDto.fromDomain(listingService.createListing(createListingDto.toDomain()));
    }
}
