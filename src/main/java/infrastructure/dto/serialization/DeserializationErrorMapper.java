package infrastructure.dto.serialization;

import infrastructure.dto.out.ErrorDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DeserializationErrorMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException e) {
        //e.printStackTrace();
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDto(e.getMessage())).build();
        //return Response
        //        .status(Response.Status.BAD_REQUEST)
        //        .entity("Failed to deserialize the request. Check your JSON payload.")
        //        .type(MediaType.TEXT_PLAIN)
        //        .build();
    }
}
