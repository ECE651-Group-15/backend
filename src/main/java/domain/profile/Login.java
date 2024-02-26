package domain.profile;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
public class Login {
	public Optional<String> email;
	public Optional<String> password;
}
