package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAgentCtvRequest {
	private String name;
	private String phone;
	private String email;
	private String description;
}
