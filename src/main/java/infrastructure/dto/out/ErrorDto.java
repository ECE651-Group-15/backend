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

    private String message;
    private Integer code;
    private Object data;

    public ErrorDto(String message, Integer code) {
        this.message = message;
        this.code = code;
        this.data = null;
    }



}
