package infrastructure.dto.serialization;


import infrastructure.dto.out.ErrorDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        ErrorDto errorDto = new ErrorDto("Runtime Error",e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDto).build();
    }
}
