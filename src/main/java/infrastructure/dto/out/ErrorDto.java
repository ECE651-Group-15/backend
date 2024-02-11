package infrastructure.dto.out;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
@Setter
@Getter
//@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private String error;
    private String message;

    public ErrorDto(String message) {
        this.error = "General Error";
        this.message = message;
    }

    // Getters and setters

}
