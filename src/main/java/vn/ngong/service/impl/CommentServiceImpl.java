package vn.ngong.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Comment;
import vn.ngong.repository.CommentRepository;
import vn.ngong.request.CommentInsertRequest;
import vn.ngong.response.CommentListResponse;
import vn.ngong.service.CommentService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentListResponse list(int productId, int orderType, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Comment> comments = null;
        if (orderType == 0) {
            comments = commentRepository.findAllByProductIdAndStatusOrderByUpdatedAtDesc(productId,1, pageable);
        }
        if (orderType == 1) {
            comments = commentRepository.findAllByProductIdAndStatusOrderByUpdatedAt(productId,1, pageable);
        }
        if (orderType == 2) {
            comments = commentRepository.findAllByProductIdAndStatusOrderByRateDesc(productId,1, pageable);
        }
        if (orderType == 3) {
            comments = commentRepository.findAllByProductIdAndStatusOrderByRate(productId,1, pageable);
        }

        CommentListResponse res = CommentListResponse
                .builder()
                .comments(comments)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalItem(comments.getTotalElements())
                .build();
        res.setCode("00");
        res.setDesc("Success");
        return res;
    }

    @Override
    public Comment add(CommentInsertRequest comment) {
        Comment entity = Comment
                .builder()
                .productId(comment.getProductId())
                .email(comment.getEmail())
                .rate(comment.getRate())
                .title(comment.getTitle())
                .comment(comment.getComment())
                .phoneNumber(comment.getPhoneNumber())
                .parrentId(comment.getParrentId())
                .userName(comment.getUserName())
                .images(comment.getImages())
                .video(comment.getVideo())
                .status(1)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        commentRepository.save(entity);

        return entity;
    }

    @Override
    public CommentListResponse listByCategory(int categoryId, int orderType, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Comment> comments = null;
        if (orderType == 0) {
            comments = commentRepository.findAllByIsCategoryAndCategoryIdAndStatusOrderByUpdatedAtDesc(true, categoryId,1, pageable);
        }
        if (orderType == 1) {
            comments = commentRepository.findAllByIsCategoryAndCategoryIdAndStatusOrderByUpdatedAt(true, categoryId,1, pageable);
        }
        if (orderType == 2) {
            comments = commentRepository.findAllByIsCategoryAndCategoryIdAndStatusOrderByRateDesc(true, categoryId,1, pageable);
        }
        if (orderType == 3) {
            comments = commentRepository.findAllByIsCategoryAndCategoryIdAndStatusOrderByRate(true, categoryId,1, pageable);
        }

        CommentListResponse res = CommentListResponse
                .builder()
                .comments(comments)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .totalItem(comments.getTotalElements())
                .build();
        res.setCode("00");
        res.setDesc("Success");
        return res;
    }

    @Override
    public Comment addByCategory(CommentInsertRequest comment) {
        Comment entity = Comment
                .builder()
                .productId(comment.getProductId())
                .email(comment.getEmail())
                .rate(comment.getRate())
                .title(comment.getTitle())
                .comment(comment.getComment())
                .phoneNumber(comment.getPhoneNumber())
                .parrentId(comment.getParrentId())
                .userName(comment.getUserName())
                .images(comment.getImages())
                .video(comment.getVideo())
                .isCategory(true)
                .categoryId(comment.getCategoryId())
                .status(1)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        commentRepository.save(entity);

        return entity;
    }
}
