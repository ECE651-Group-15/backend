package infrastructure.dto.out.profile;

import domain.profile.CustomerProfile;
import lombok.Builder;

import java.util.List;
import java.util.Optional;

@Builder
public record CustomerProfileDetailsInternalDto(String id,
                                                String email,
                                                String password,
                                                String name,
                                                String avatar,
                                                Optional<String> phone,
                                                Optional<Double> longitude,
                                                Optional<Double> latitude,
                                                List<String> postedListingIds,
                                                List<String> starredListingIds) {
	public static CustomerProfileDetailsInternalDto fromDomain(CustomerProfile customerProfile) {
		return CustomerProfileDetailsInternalDto.builder()
		                                        .id(customerProfile.getId())
		                                        .name(customerProfile.getName())
		                                        .password(customerProfile.getPassword())
		                                        .email(customerProfile.getEmail())
		                                        .avatar(customerProfile.getAvatar())
		                                        .phone(customerProfile.getPhone())
		                                        .longitude(customerProfile.getLongitude())
		                                        .latitude(customerProfile.getLatitude())
		                                        .starredListingIds(customerProfile.getStarredListingIds().orElse(List.of()))
		                                        .postedListingIds(customerProfile.getPostedListingIds().orElse(List.of()))
		                                        .build();
	}
}
