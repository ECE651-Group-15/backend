package infrastructure.dto.serialization;
import com.fasterxml.jackson.databind.JsonMappingException;

import infrastructure.dto.ApiResponse;
import infrastructure.dto.out.listing.ListingDetailsDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Optional;

@Provider
public class DeserializationErrorMapper implements ExceptionMapper<JsonMappingException> {
    @Override
    public Response toResponse(JsonMappingException e) {
        String message = "Error processing JSON data. Please check the request payload.";
        ApiResponse<ListingDetailsDto> response = new ApiResponse<>(Optional.of(message),
                400,
                Optional.empty());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}