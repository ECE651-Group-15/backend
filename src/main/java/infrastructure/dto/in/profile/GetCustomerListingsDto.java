package infrastructure.dto.in.profile;

import java.util.Optional;

public record GetCustomerListingsDto(String customerId,
                                     Integer page,
                                     Optional<Integer> pageSize) {
}
