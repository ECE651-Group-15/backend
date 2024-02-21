package infrastructure.result;

import domain.profile.CustomerProfile;
import lombok.Builder;

import java.util.Optional;

@Builder(toBuilder = true)
public record DeleteCustomerResult (boolean hasCreatedListings,
                                    Optional<CustomerProfile> deletedCustomerProfile) {
}
