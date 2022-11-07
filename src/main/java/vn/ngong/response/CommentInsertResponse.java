package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import vn.ngong.entity.Comment;

@Setter
@Getter
@SuperBuilder(toBuilder = true)
@Jacksonized
public class CommentInsertResponse extends BaseResponse {
    private Comment comment;
}
