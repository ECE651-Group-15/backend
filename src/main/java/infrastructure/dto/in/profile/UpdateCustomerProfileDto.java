package infrastructure.dto.in.profile;

import domain.profile.UpdateCustomerProfile;
import lombok.Builder;

import java.util.Optional;

@Builder(toBuilder = true)
public record UpdateCustomerProfileDto(String id,
                                       String name,
                                       String password,
                                       String email,
                                       Optional<String> avatar,
                                       Optional<String> phone,
                                       Optional<Double> longitude,
                                       Optional<Double> latitude) {
    public UpdateCustomerProfile toDomain() {
        return UpdateCustomerProfile.builder()
                                    .id(id)
                                    .name(name)
                                    .password(password)
                                    .email(email)
                                    .avatar(avatar)
                                    .phone(phone)
                                    .longitude(longitude)
                                    .latitude(latitude)
                                    .build();
    }
}
