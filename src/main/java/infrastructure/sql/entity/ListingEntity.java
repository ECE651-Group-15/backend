package infrastructure.sql.entity;

import domain.listing.Category;
import domain.listing.ListingDetails;
import domain.listing.ListingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder(toBuilder = true)
public class ListingEntity {
    @Id
    private String id;

    private String title;
    private String description;
    private Double price;

    private Double longitude;
    private Double latitude;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "customer_profile_id")
    private CustomerProfileEntity customerProfile;

    @Enumerated(EnumType.STRING)
    private ListingStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;

    @ManyToMany
    @JoinTable(
            name = "listing_starred_by_customers",
            joinColumns = @JoinColumn(name = "listing_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_profile_id")
    )
    private List<CustomerProfileEntity> customersWhoStarred;

    private Long createdAt;
    private Long updatedAt;

    public static ListingEntity fromDomain(ListingDetails listingDetails,
                                           CustomerProfileEntity ownerProfile) {
        ListingEntity entity = new ListingEntity();
        entity.setId(listingDetails.getId());
        entity.setTitle(listingDetails.getTitle());
        entity.setDescription(listingDetails.getDescription());
        entity.setPrice(listingDetails.getPrice().orElse(null));
        entity.setLongitude(listingDetails.getLongitude());
        entity.setLatitude(listingDetails.getLatitude());
        entity.setCategory(listingDetails.getCategory());
        entity.setCustomerProfile(ownerProfile);
        entity.setStatus(listingDetails.getStatus());
        entity.setImages(listingDetails.getImages());
        entity.setCreatedAt(listingDetails.getCreatedAt());
        entity.setUpdatedAt(listingDetails.getUpdatedAt());
        return entity;
    }

    public ListingDetails toDomain() {
        return ListingDetails.builder()
                             .id(this.id)
                             .title(this.title)
                             .description(this.description)
                             .price(Optional.ofNullable(this.price))
                             .longitude(this.longitude)
                             .latitude(this.latitude)
                             .category(this.category)
                             .customerId(this.customerProfile != null ? this.customerProfile.getId() : null)
                             .status(this.status)
                             .images(this.images)
                             .createdAt(this.createdAt)
                             .updatedAt(this.updatedAt)
                             .customersWhoStarred(this.customersWhoStarred != null
                                                          ? this.customersWhoStarred.stream()
                                                                                    .map(CustomerProfileEntity::getId)
                                                                                    .toList()
                                                          : List.of())
                             .build();
    }

}
