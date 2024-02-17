package infrastructure.dto.in.profile;

import java.util.Optional;

public record GetCustomerPostedListingsDto(String customerId,
                                           Integer page,
                                           Optional<Integer> pageSize) {
}
