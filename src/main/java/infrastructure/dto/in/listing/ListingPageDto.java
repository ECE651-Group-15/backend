package infrastructure.dto.in.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ListingPageDto {
    public final Integer page;
    public final Integer pageSize;
}
