package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Post;
import vn.ngong.repository.PostRepository;
import vn.ngong.service.PostService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepository postRepository;

	@Override
	public List<Post> findPostByMenu(String menuCode, int limit, int offset) {
		List<Post> returnPostList = new ArrayList<>();
		List<Post> postList = postRepository.findPostByMenuCode(menuCode, limit, offset);
		if (postList.isEmpty()) {
			return null;
		}
		for (Post p : postList) {
			List<Post> lastPostList = postRepository.findAllLastPostByParentPost(p.getId());
			if (lastPostList.isEmpty()) {
				continue;
			}
			Post post = lastPostList.get(0);
			returnPostList.add(post);
		}

		return returnPostList;
	}
}
