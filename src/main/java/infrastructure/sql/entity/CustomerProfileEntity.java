package infrastructure.sql.entity;

import domain.profile.CustomerProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_profile")
@Builder(toBuilder = true)
public class CustomerProfileEntity {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private Double longitude;
    private Double latitude;

    @OneToMany(mappedBy = "customerProfile")
    private List<ListingEntity> postedListings;

    @ManyToMany(mappedBy = "customersWhoStarred")
    private List<ListingEntity> starredListings;

    public static CustomerProfileEntity fromDomain(CustomerProfile customerProfile) {
        CustomerProfileEntity entity = new CustomerProfileEntity();
        entity.setId(customerProfile.getId());
        entity.setName(customerProfile.getName());
        entity.setEmail(customerProfile.getEmail().orElse(null));
        entity.setPhone(customerProfile.getPhone().orElse(null));
        entity.setLongitude(customerProfile.getLongitude().orElse(null));
        entity.setLatitude(customerProfile.getLatitude().orElse(null));
        return entity;
    }

    public static void updateFromEntity(CustomerProfileEntity entity) {
        entity.setName(entity.getName());
        entity.setEmail(entity.getEmail());
        entity.setPhone(entity.getPhone());
        entity.setLongitude(entity.getLongitude());
        entity.setLatitude(entity.getLatitude());
        entity.setPostedListings(entity.getPostedListings());
        entity.setStarredListings(entity.getStarredListings());
    }

    public CustomerProfile toDomain() {
        CustomerProfile profile = CustomerProfile.builder()
                                                 .id(this.id)
                                                 .name(this.name)
                                                 .email(Optional.of(this.email))
                                                 .phone(Optional.of(this.phone))
                                                 .longitude(Optional.of(this.longitude))
                                                 .latitude(Optional.of(this.latitude))
                                                 .build();
        if (this.postedListings != null) {
            List<String> listingIds = this.postedListings.stream()
                                                         .map(ListingEntity::getId)
                                                         .collect(toList());
            profile = profile.toBuilder()
                             .postedListingIds(Optional.of(listingIds))
                             .build();
        }
        if (this.starredListings != null) {
            List<String> starredListingIds = this.starredListings.stream()
                                                                 .map(ListingEntity::getId)
                                                                 .collect(toList());
            profile = profile.toBuilder()
                             .starredListingIds(Optional.of(starredListingIds))
                             .build();
        }
        return profile;
    }
}
