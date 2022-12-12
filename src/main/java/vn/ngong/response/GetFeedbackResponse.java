package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.AgentCTV;
import vn.ngong.entity.Feedback;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetFeedbackResponse extends BaseResponse {
	private List<Feedback> feedbackList;
}
