package infrastructure.dto.out.profile;

import domain.profile.CustomerProfile;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CustomerProfilesDetailsDto(String id,
                                         String name,
                                         Optional<String> email,
                                         Optional<String> phone,
                                         Optional<Double> longitude,
                                         Optional<Double> latitude,
                                         List<String> postedListingIds,
                                         List<String> starredListIds) {
    public static CustomerProfilesDetailsDto fromDomain(CustomerProfile customerProfile) {
        return CustomerProfilesDetailsDto.builder()
                                         .id(customerProfile.getId())
                                         .name(customerProfile.getName())
                                         .email(customerProfile.getEmail())
                                         .phone(customerProfile.getPhone())
                                         .longitude(customerProfile.getLongitude())
                                         .latitude(customerProfile.getLatitude())
                                         .starredListIds(customerProfile.getStarredListingIds().orElse(List.of()))
                                         .postedListingIds(customerProfile.getPostedListingIds().orElse(List.of()))
                                         .build();
    }
}
