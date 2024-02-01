package infrastructure;

import api.dto.out.ListingDetailsDto;
import domain.ListingService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ListingManagementResource {

    public ListingService listingService;
    @Path("hello")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ListingDetailsDto getListing(String listingId) {
        return ListingDetailsDto.fromDomain(listingService.getListing(listingId));
    }

    @Path("hello1")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getListing() {
        return "Hello";
    }
}
