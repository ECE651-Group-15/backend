package infrastructure.dto.in.profile;


import domain.profile.Login;
import lombok.Builder;

import java.util.Optional;

@Builder
public record LoginDto (Optional<String> email,
						Optional<String> password) {
	public Login toDomain() {
		return Login.builder()
					.email(email)
					.password(password)
					.build();
	}
}
