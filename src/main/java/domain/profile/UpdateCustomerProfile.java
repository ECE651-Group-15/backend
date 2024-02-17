package domain.profile;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder(toBuilder = true)
public class UpdateCustomerProfile {
    String id;
    String email;
    String password;
    String name;
    Optional<String> phone;
    Optional<Double> longitude;
    Optional<Double> latitude;
}
