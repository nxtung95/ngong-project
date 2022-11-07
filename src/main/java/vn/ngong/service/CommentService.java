package vn.ngong.service;

import vn.ngong.entity.Comment;
import vn.ngong.request.CommentInsertRequest;
import vn.ngong.response.CommentListResponse;

import java.util.List;

public interface CommentService {
    CommentListResponse list(int orderType, int pageIndex, int pageSize);
    Comment add(CommentInsertRequest comment);
}
