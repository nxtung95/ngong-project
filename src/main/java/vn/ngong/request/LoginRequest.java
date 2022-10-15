package vn.ngong.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequest implements Serializable {
	@Schema(required = true, example = "User: user_ngong@gmail.com\n Admin: admin_ngong@gmail.com")
	private String username;
	@Schema(required = true, example = "1234")
	private String password;
}