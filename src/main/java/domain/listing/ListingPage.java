package domain.listing;

import lombok.Data;

import java.util.List;


@Data
public class ListingPage {
    private List<ListingDetails> listings; // list of listing
    private Integer pageIndex; // current page
    private Integer pageSize; //
    private Integer totalPages; //

}
