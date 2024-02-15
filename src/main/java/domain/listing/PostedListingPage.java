package domain.listing;

import lombok.Value;

import java.util.List;

@Value
public class PostedListingPage {
    List<ListingDetails> postedListings;
}
