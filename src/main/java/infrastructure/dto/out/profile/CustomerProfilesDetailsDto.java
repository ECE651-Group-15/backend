package infrastructure.dto.out.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import domain.profile.CustomerProfile;
import lombok.Builder;

import java.util.Optional;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerProfilesDetailsDto(String id,
                                         String name,
                                         Optional<String> email,
                                         Optional<String> phone,
                                         Optional<Double> longitude,
                                         Optional<Double> latitude) {
    public static CustomerProfilesDetailsDto fromDomain(CustomerProfile customerProfile) {
        return CustomerProfilesDetailsDto.builder()
                                         .id(customerProfile.getId())
                                         .name(customerProfile.getName())
                                         .email(customerProfile.getEmail())
                                         .phone(customerProfile.getPhone())
                                         .longitude(customerProfile.getLongitude())
                                         .latitude(customerProfile.getLatitude())
                                         .build();
    }
}
