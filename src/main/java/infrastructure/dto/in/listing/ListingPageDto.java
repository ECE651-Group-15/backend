package infrastructure.dto.in.listing;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Optional;

@AllArgsConstructor
@Builder
public class ListingPageDto {
    public final Integer page;
    public final Optional<Integer> pageSize;
}
