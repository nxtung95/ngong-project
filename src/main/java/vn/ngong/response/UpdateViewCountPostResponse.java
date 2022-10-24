package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.ViewCountPost;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class UpdateViewCountPostResponse extends BaseResponse {
	private ViewCountPost post;
}
