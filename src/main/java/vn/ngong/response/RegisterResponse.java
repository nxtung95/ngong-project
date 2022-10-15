package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.User;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class RegisterResponse extends BaseResponse {
	private User user;
}
