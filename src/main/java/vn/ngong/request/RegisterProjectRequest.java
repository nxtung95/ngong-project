package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterProjectRequest {
	private String name;
	private String phone;
	private String email;
	private int projectId;
	private String feedback;
}
