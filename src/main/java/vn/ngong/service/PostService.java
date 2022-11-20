package vn.ngong.service;

import vn.ngong.entity.Post;
import vn.ngong.entity.ViewCountPost;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
	List<Post> findPostByMenu(String menuCode, int limit, int offset);

	ViewCountPost updateViewCount(int postId, HttpServletRequest httpServletRequest);

	List<Post> findLastTopPost(int size);

	List<Post> findLastTopPostOrderByMonth(int size, int orderBy);
}
