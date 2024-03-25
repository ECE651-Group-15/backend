package infrastructure.result;

import infrastructure.sql.entity.CustomerProfileEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class RequireUserResult {
	private boolean IdNotExists;
	private boolean PasswordIsWrong;
	private Optional<CustomerProfileEntity> UpdatedRequireUserResult;
}
