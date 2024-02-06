package infrastructure.sql.entity;

import domain.listing.Category;
import domain.listing.ListingDetails;
import domain.listing.ListingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "listing")
@Getter
@Setter
@NoArgsConstructor
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
    private Instant createdAt;
    private Instant updatedAt;

    public static ListingEntity fromDomain(ListingDetails listingDetails) {
        ListingEntity listingEntity = new ListingEntity();
        listingEntity.setId(listingDetails.getId());
        listingEntity.setTitle(listingDetails.getTitle());
        listingEntity.setDescription(listingDetails.getDescription());
        listingEntity.setPrice(listingDetails.getPrice().orElse(-1.0));
        listingEntity.setLongitude(listingDetails.getLongitude());
        listingEntity.setLatitude(listingDetails.getLatitude());
        listingEntity.setCategory(listingDetails.getCategory());
        listingEntity.setUserId(listingDetails.getUserId());
        listingEntity.setStatus(listingDetails.getStatus());
        listingEntity.setImages(listingDetails.getImages());
        listingEntity.setStarCount(listingDetails.getStarCount());
        listingEntity.setCreatedAt(listingDetails.getCreatedAt());
        listingEntity.setUpdatedAt(listingDetails.getUpdatedAt());
        return listingEntity;
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
