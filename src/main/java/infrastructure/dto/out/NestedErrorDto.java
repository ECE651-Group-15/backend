package infrastructure.dto.out;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class NestedErrorDto {
    private Integer code;
    private ErrorDto data;
}
