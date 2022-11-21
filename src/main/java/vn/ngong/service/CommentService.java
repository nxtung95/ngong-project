package vn.ngong.service;

import vn.ngong.entity.Comment;
import vn.ngong.request.CommentInsertRequest;
import vn.ngong.response.CommentListResponse;

import java.util.List;

public interface CommentService {
    CommentListResponse list(int productId, int orderType, int pageIndex, int pageSize);
    Comment add(CommentInsertRequest comment);

    CommentListResponse listByCategory(int categoryId, int orderType, int pageIndex, int pageSize);
    Comment addByCategory(CommentInsertRequest comment);
}
