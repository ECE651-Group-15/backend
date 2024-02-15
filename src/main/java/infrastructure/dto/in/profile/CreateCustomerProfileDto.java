package infrastructure.dto.in.profile;

import domain.profile.CreateCustomerProfile;
import lombok.Builder;

import java.util.Optional;

@Builder
public record CreateCustomerProfileDto(String name,
                                       String email,
                                       String password,
                                       Optional<String> phone,
                                       Optional<Double> longitude,
                                       Optional<Double> latitude) {

    public CreateCustomerProfile toDomain() {
        return CreateCustomerProfile.builder()
                                    .name(name)
                                    .password(password)
                                    .email(email)
                                    .phone(phone)
                                    .longitude(longitude)
                                    .latitude(latitude)
                                    .build();
    }
}
