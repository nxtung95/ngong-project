package vn.ngong.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByStatusByOrderByRate(int status, Pageable pageable);
    Page<Comment> findAllByStatusByOrderByRateDesc(int status, Pageable pageable);
    Page<Comment> findAllByStatusByOrderByUpdatedAt(int status, Pageable pageable);
    Page<Comment> findAllByStatusByOrderByUpdatedAtDesc(int status, Pageable pageable);
}
