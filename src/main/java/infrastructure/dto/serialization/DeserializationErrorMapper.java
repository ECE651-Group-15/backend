package infrastructure.dto.serialization;
import com.fasterxml.jackson.databind.JsonMappingException;

import infrastructure.dto.out.ErrorDto;
import infrastructure.dto.out.NestedErrorDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DeserializationErrorMapper implements ExceptionMapper<JsonMappingException> {
    @Override
    public Response toResponse(JsonMappingException e) {
        String message = "Error processing JSON data. Please check the request payload.";
        ErrorDto innerError = new ErrorDto(message, 400);
        NestedErrorDto nestedError = new NestedErrorDto(200, innerError);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(nestedError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}