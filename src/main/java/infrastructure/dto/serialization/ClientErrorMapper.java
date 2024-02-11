package infrastructure.dto.serialization;


import infrastructure.dto.out.ErrorDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ClientErrorMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException e) {
        ErrorDto errorDto = new ErrorDto("Validation Error",e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(errorDto).build();
    }
}

