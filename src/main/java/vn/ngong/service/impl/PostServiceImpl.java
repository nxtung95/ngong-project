package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Post;
import vn.ngong.repository.MenuRepository;
import vn.ngong.repository.PostRepository;
import vn.ngong.service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<Post> findPostByMenu(String menuCode, int limit, int offset) {
		List<Post> returnPostList = new ArrayList<>();
		List<Post> postList = postRepository.findPostByMenuCode(menuCode, limit, offset);
		if (postList.isEmpty()) {
			return null;
		}
		Map<Integer, String> imageMap = menuRepository.findAllImageRepresent();
		Map<Integer, String> descriptionMap = menuRepository.findAllDescription();

		for (Post p : postList) {
			List<Post> lastPostList = postRepository.findAllLastPostByParentPost(p.getId());
			if (lastPostList.isEmpty()) {
				continue;
			}
			Post post = lastPostList.get(0);
			String image = "";
			if (imageMap.containsKey(p.getId())) {
				image = imageMap.get(p.getId());
			}
			String description = "";
			if (descriptionMap.containsKey(p.getId())) {
				description = descriptionMap.get(p.getId());
			}
			post.setMenuImage(image);
			post.setDescription(description);
			returnPostList.add(post);
		}

		return returnPostList;
	}
}
