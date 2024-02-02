package infrastructure;

import domain.listing.ListingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ListingResources {

    @Inject
    public ListingService listingService;

    @Path("hello")
    @GET
    public String getListing(String listingId) {
        return listingService.getListing(listingId);
    }
}
