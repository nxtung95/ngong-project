package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ngong.cache.LocalCacheConfig;
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
	@Autowired
	private LocalCacheConfig localCacheConfig;

	@Override
	public List<Post> findPostByMenu(String menuCode, int limit, int offset) {
		List<Post> returnPostList = new ArrayList<>();
		List<Post> postList = postRepository.findPostByMenuCode(menuCode, limit, offset);
		if (postList.isEmpty()) {
			return null;
		}
		if (localCacheConfig.getImageMap().isEmpty()) {
			localCacheConfig.loadCacheAllImagePost();
		}
		Map<Integer, String> imageMap = localCacheConfig.getImageMap();

		if (localCacheConfig.getDescriptionMap().isEmpty()) {
			localCacheConfig.loadCacheAllDescriptionPost();
		}
		Map<Integer, String> descriptionMap = localCacheConfig.getDescriptionMap();

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
