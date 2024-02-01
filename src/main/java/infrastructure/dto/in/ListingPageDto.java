package infrastructure.dto.in;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ListingPageDto {
    public final Integer page;
    public final Integer pageSize;
}
