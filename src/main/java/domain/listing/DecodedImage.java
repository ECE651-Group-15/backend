package domain.listing;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecodedImage {
    byte[] image;
}
