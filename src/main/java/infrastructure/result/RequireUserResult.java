package infrastructure.result;

import infrastructure.sql.entity.CustomerProfileEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class RequireUserResult {
	boolean idNotExists;
	boolean passwordIsWrong;
	Optional<CustomerProfileEntity> updatedRequireUser;
}
