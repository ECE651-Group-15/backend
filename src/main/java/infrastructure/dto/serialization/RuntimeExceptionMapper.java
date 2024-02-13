package infrastructure.dto.serialization;


import infrastructure.dto.ApiResponse;
import infrastructure.dto.out.listing.ListingDetailsDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Optional;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        String message = e.getMessage();
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.of(message),
                500,
                Optional.empty());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
