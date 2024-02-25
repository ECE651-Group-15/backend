package infrastructure.result;

import domain.profile.CustomerProfile;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder(toBuilder = true)
public class CustomerUnStarResult {
	boolean listingNotFound;
	boolean customerNotFound;
	Optional<CustomerProfile> customerProfile;
}
