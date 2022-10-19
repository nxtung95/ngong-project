package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.Post;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class FindPostByMenuResponse extends BaseResponse {
	private List<Post> postList;
}
