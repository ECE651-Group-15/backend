package infrastructure.sql.entity;

import domain.profile.CustomerProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_profile")
public class CustomerProfileEntity {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private Double longitude;
    private Double latitude;

    public static CustomerProfileEntity fromDomain(CustomerProfile customerProfile) {
        return new CustomerProfileEntity(customerProfile.getId(),
                                         customerProfile.getName(),
                                         customerProfile.getEmail().orElse(null),
                                         customerProfile.getPhone().orElse(null),
                                         customerProfile.getLongitude().orElse(null),
                                         customerProfile.getLatitude().orElse(null));
    }

    public static void updateFromEntity(CustomerProfileEntity entity) {
        entity.setName(entity.getName());
        entity.setEmail(entity.getEmail());
        entity.setPhone(entity.getPhone());
        entity.setLongitude(entity.getLongitude());
        entity.setLatitude(entity.getLatitude());
    }

    public CustomerProfile toDomain() {
        return CustomerProfile.builder()
                              .id(this.id)
                              .name(this.name)
                              .email(Optional.of(this.email))
                              .phone(Optional.of(this.phone))
                              .longitude(Optional.of(this.longitude))
                              .latitude(Optional.of(this.latitude))
                              .build();
    }
}
