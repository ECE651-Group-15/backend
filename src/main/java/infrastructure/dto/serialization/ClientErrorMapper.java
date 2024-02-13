package infrastructure.dto.serialization;


import infrastructure.dto.ApiResponse;
import infrastructure.dto.out.listing.ListingDetailsDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Optional;

@Provider
public class ClientErrorMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException e) {
        String message = e.getMessage();
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.of(message),
                4001,
                Optional.empty());


        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

