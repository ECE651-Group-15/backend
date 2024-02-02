package domain.listing;

import lombok.Data;

import java.util.Optional;

@Data
public class ListingPage {
    public Integer page;
    public Optional<Integer> pageSize;
}
