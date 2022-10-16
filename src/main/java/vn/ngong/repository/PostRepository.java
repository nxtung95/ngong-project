package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
	@Query(value = "select a.* from wp_posts a " +
			"INNER JOIN wp_term_relationships b ON a.ID = b.object_id " +
			"INNER JOIN wp_term_taxonomy c ON b.term_taxonomy_id = c.term_taxonomy_id " +
			"INNER JOIN wp_terms d ON c.term_id = d.term_id " +
			"WHERE d.slug = :menuCode AND c.taxonomy = 'category'", nativeQuery = true)
	List<Post> findPostByMenuCode(@Param("menuCode") String menuCode);

	@Query(value = "SELECT * FROM wp_posts WHERE post_parent = :postParent", nativeQuery = true)
	List<Post> findAllLastPostByParentPost(@Param("postParent") int parentPostId);
}
