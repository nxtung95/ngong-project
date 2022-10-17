package vn.ngong.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RegisterRequest {
	@Schema(description = "tên", required = true)
	private String name;
	@Schema(description = "sđt", required = true)
	private String phone;
	@Schema(description = "email", required = true)
	private String email;
	@Schema(description = "password", required = true)
	private String password;
	private String address;
}
