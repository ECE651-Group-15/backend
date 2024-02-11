package infrastructure.dto.out;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ErrorDto {

    private String error;
    private String message;

    public ErrorDto(String message) {
        this.error = "Validation Error";
        this.message = message;
    }

    // Getters and setters

}
