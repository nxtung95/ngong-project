package vn.ngong.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Repository
@Slf4j
public class PostNativeRepository {
	@PersistenceContext
	private EntityManager entityManager;

	public Post findNewestPostById(int postId) {
		Post post;
		try {
			Query query = entityManager.createNativeQuery("select a.*, d.slug from wp_posts a " +
					"INNER JOIN wp_term_relationships b ON a.ID = b.object_id " +
					"INNER JOIN wp_term_taxonomy c ON b.term_taxonomy_id = c.term_taxonomy_id " +
					"INNER JOIN wp_terms d ON c.term_id = d.term_id " +
					"WHERE c.taxonomy = 'category' AND a.ID = :postId " +
					"ORDER BY a.post_date DESC ");
			query.setParameter("postId", postId);
			Object[] obj = (Object[]) query.getSingleResult();
			post = Post.builder()
					.id(Integer.parseInt((obj[0]).toString()))
					.slug(obj[23].toString())
					.build();
			return post;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
