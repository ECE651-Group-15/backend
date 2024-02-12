package infrastructure.dto.serialization;


import infrastructure.dto.out.ErrorDto;
import infrastructure.dto.out.NestedErrorDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        ErrorDto innerError = new ErrorDto(e.getMessage(), 500);
        NestedErrorDto nestedError = new NestedErrorDto(200, innerError);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(nestedError)
                .build();
    }
}
