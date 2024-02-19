package infrastructure.dto.out.profile;

import lombok.Builder;

import java.util.List;

@Builder
public record CustomerProfilePageDto(List<CustomerProfilesDetailsDto> customerProfiles) {
}
