package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.Question;
import vn.ngong.entity.RegisterProject;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class AskQuestionResponse extends BaseResponse {
	private Question question;
}
