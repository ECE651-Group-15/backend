package infrastructure.sql.entity;

import domain.listing.Category;
import domain.listing.ListingDetails;
import domain.listing.ListingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "listing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ListingEntity {
    @Id
    private String id;

    private String title;
    private String description;
    private Double price; // Assuming price is mandatory for simplicity

    private Double longitude;
    private Double latitude;

    @Enumerated(EnumType.STRING)
    private Category category;
    private String userId;

    @Enumerated(EnumType.STRING)
    private ListingStatus status;

    @ElementCollection
    private List<String> images;

    private int starCount;
    private Long createdAt;
    private Long updatedAt;

    public static ListingEntity fromDomain(ListingDetails listingDetails) {
        return new ListingEntity(listingDetails.getId(),
                                 listingDetails.getTitle(),
                                 listingDetails.getDescription(),
                                 listingDetails.getPrice().orElse(null),
                                 listingDetails.getLongitude(),
                                 listingDetails.getLatitude(),
                                 listingDetails.getCategory(),
                                 listingDetails.getUserId(),
                                 listingDetails.getStatus(),
                                 listingDetails.getImages(),
                                 listingDetails.getStarCount(),
                                 listingDetails.getCreatedAt(),
                                 listingDetails.getUpdatedAt()
        );
    }

    public static void updateFromEntity(ListingEntity entity) {
        entity.setId(entity.getId());
        entity.setTitle(entity.getTitle());
        entity.setDescription(entity.getDescription());
        entity.setPrice(entity.getPrice());
        entity.setLongitude(entity.getLongitude());
        entity.setLatitude(entity.getLatitude());
        entity.setCategory(entity.getCategory());
        entity.setUserId(entity.getUserId());
        entity.setStatus(entity.getStatus());
        entity.setImages(entity.getImages());
        entity.setStarCount(entity.getStarCount());
        entity.setCreatedAt(entity.getCreatedAt());
        entity.setUpdatedAt(entity.getUpdatedAt());
    }

    public ListingDetails toDomain() {
        return ListingDetails.builder()
                             .id(this.id)
                             .title(this.title)
                             .description(this.description)
                             .price(Optional.of(this.price))
                             .longitude(this.longitude)
                             .latitude(this.latitude)
                             .category(this.category)
                             .userId(this.userId)
                             .status(this.status)
                             .images(this.images)
                             .starCount(this.starCount)
                             .createdAt(this.createdAt)
                             .updatedAt(this.updatedAt)
                             .build();
    }
}
