package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AskQuestionRequest {
	private String name;
	private String phone;
	private String email;
	private String content;
}
