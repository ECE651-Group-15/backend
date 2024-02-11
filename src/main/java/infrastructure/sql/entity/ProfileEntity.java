package infrastructure.sql.entity;

import domain.profile.CustomerProfile;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private Double longitude;

    @Column(nullable = true)
    private Double latitude;



    @ElementCollection
    private List<String> starredListingIds;


    public static ProfileEntity fromDomain(CustomerProfile customerProfile) {
        ProfileEntity entity = new ProfileEntity();
        entity.setId(Long.valueOf(customerProfile.getId()));
        entity.setName(customerProfile.getName());
        entity.setEmail(customerProfile.getEmail().orElse(null));
        entity.setPhone(customerProfile.getPhone().orElse(null));
        entity.setLongitude(customerProfile.getLongitude().orElse(null));
        entity.setLatitude(customerProfile.getLatitude().orElse(null));
        entity.setStarredListingIds(customerProfile.getStarredListingIds().orElse(null));
        return entity;
    }


    public void updateFromEntity(ProfileEntity entity) {
        this.setName(entity.getName());
        this.setEmail(entity.getEmail());
        this.setPhone(entity.getPhone());
        this.setLongitude(entity.getLongitude());
        this.setLatitude(entity.getLatitude());
        this.setStarredListingIds(entity.getStarredListingIds());
    }


    public CustomerProfile toDomain() {
        return CustomerProfile.builder()
                .id(String.valueOf(this.id))
                .name(this.name)
                .email(java.util.Optional.ofNullable(this.email))
                .phone(java.util.Optional.ofNullable(this.phone))
                .longitude(java.util.Optional.ofNullable(this.longitude))
                .latitude(java.util.Optional.ofNullable(this.latitude))
                .starredListingIds(java.util.Optional.ofNullable(this.starredListingIds))
                .build();
    }

}
