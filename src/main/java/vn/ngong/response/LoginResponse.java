package vn.ngong.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.User;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class LoginResponse extends BaseResponse {
	@Schema(name = "token", description = "Chuỗi token")
	private String jwttoken;
	private User user;
}