package vn.ngong.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByProductIdAndStatusOrderByRate(int productId, int status, Pageable pageable);
    Page<Comment> findAllByProductIdAndStatusOrderByRateDesc(int productId, int status, Pageable pageable);
    Page<Comment> findAllByProductIdAndStatusOrderByUpdatedAt(int productId, int status, Pageable pageable);
    Page<Comment> findAllByProductIdAndStatusOrderByUpdatedAtDesc(int productId, int status, Pageable pageable);
}
