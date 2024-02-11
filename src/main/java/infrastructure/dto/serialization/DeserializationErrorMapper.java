package infrastructure.dto.serialization;
import com.fasterxml.jackson.databind.JsonMappingException;

import infrastructure.dto.out.ErrorDto;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DeserializationErrorMapper implements ExceptionMapper<JsonMappingException> {
    @Override
    public Response toResponse(JsonMappingException e) {
        String message = "Error processing JSON data. Please check the request payload.";
        ErrorDto errorDto = new ErrorDto("JSON_MAPPING_ERROR",message);
        //errorDto.setError("JSON_MAPPING_ERROR");

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorDto)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
