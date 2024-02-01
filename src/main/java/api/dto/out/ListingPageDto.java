package api.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class ListingPageDto {
    public final ListingDetailsDto [] listings;
    public final Integer page;
    public final Integer pageSize;
}
