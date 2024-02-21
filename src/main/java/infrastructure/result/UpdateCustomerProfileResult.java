package infrastructure.result;

import domain.profile.CustomerProfile;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class UpdateCustomerProfileResult {
    private boolean validationError;
    private boolean customerNotFound;
    private Optional<CustomerProfile> updatedCustomerProfile;
}
