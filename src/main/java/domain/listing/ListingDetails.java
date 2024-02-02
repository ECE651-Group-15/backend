package domain.listing;

import domain.GeoPosition;
import lombok.Builder;
import lombok.Data;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class ListingDetails {
    String id;
    String title;
    String description;
    Optional<Double> price;
    GeoPosition position;
    Category category;
    String userId;
    ListingStatus status;
    List<DecodedImage> images;
    int starCount;
    String createdAt;
    String updatedAt;

    public static DecodedImage decodeImage(String imageDataString) {
        byte[] imageByteArray = null;
        try {
            imageByteArray = Base64.getDecoder().decode(imageDataString);
        } catch (Exception e) {
            System.out.println("Exception while converting the Image " + e);
        }
        return DecodedImage.builder()
                           .image(imageByteArray)
                           .build();
    }

    public static String encodeImage(DecodedImage imageByteArray) {
        return Base64.getEncoder().encodeToString(imageByteArray.getImage());
    }
}
