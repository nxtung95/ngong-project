package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.domain.Page;
import vn.ngong.entity.Comment;

import java.util.List;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@Jacksonized
public class CommentListResponse extends BaseResponse {
    private Page<Comment> comments;
    private int pageIndex;
    private int pageSize;
    private long totalItem;
}
