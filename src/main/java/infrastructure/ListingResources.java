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

import java.util.Optional;

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
    public Optional<ListingDetailsDto> getListing(@PathParam("listingId") String listingId) {
        return Optional.ofNullable(ListingDetailsDto.fromDomain(listingService.getListing(listingId)
                                                                              .orElseThrow(() -> new RuntimeException(
                                                                                      "Cannot find listing with id " + listingId + "."))));
    }

    @POST
    @Path("/update-listing")
    public Optional<ListingDetailsDto> updateListing(UpdateListingDto updateListingDto) {
        return Optional.ofNullable(ListingDetailsDto.fromDomain(listingService.updateListing(updateListingDto.toDomain())
                                                                              .orElseThrow(() -> new RuntimeException(
                                                                                      "Cannot update listing with id " + updateListingDto.id() + "."))));
    }

    @POST
    @Path("/delete-listing/{listingId}")
    public Optional<ListingDetailsDto> updateListing(@PathParam("listingId") String listingId) {
        return Optional.ofNullable(ListingDetailsDto.fromDomain(listingService.deleteListing(listingId)
                                                                              .orElseThrow(() -> new RuntimeException(
                                                                                      "Cannot find listing with id " + listingId + "."))));
    }
}
