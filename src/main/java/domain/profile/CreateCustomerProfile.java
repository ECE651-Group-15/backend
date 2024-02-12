package domain.profile;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder(toBuilder = true)
public class CreateCustomerProfile {
    String name;
    Optional<String> email;
    Optional<String> phone;
    Optional<Double> longitude;
    Optional<Double> latitude;
}
