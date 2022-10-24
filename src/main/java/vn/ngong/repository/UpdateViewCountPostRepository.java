package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.ViewCountPost;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpdateViewCountPostRepository extends JpaRepository<ViewCountPost, Integer> {

	Optional<ViewCountPost> findByPostId(int postId);

	List<ViewCountPost> findAllByOrderByViewCountDescUpdatedDateDesc();
}
