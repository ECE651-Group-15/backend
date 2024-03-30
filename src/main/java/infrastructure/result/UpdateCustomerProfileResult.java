package infrastructure.result;

import domain.profile.CustomerProfile;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class UpdateCustomerProfileResult {
	boolean validationError;
    boolean customerNotFound;
    Optional<CustomerProfile> updatedCustomerProfile;
}
