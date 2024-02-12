package infrastructure.dto.serialization;


import infrastructure.dto.out.ErrorDto;
import infrastructure.dto.out.NestedErrorDto;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ClientErrorMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException e) {
        ErrorDto innerError = new ErrorDto(e.getMessage(), 4001);
        NestedErrorDto nestedError = new NestedErrorDto(200, innerError);
        return Response.status(Response.Status.OK)
                .entity(nestedError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

