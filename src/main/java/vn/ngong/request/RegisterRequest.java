package vn.ngong.request;

import lombok.Getter;

@Getter
public class RegisterRequest {
	private String name;
	private String phone;
	private String email;
	private String password;
}
