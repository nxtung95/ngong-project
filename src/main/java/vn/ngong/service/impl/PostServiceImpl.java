package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vn.ngong.cache.LocalCacheConfig;
import vn.ngong.entity.Post;
import vn.ngong.entity.User;
import vn.ngong.entity.ViewCountPost;
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.FormatUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.MenuRepository;
import vn.ngong.repository.PostNativeRepository;
import vn.ngong.repository.PostRepository;
import vn.ngong.repository.UpdateViewCountPostRepository;
import vn.ngong.service.PostService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostNativeRepository postNativeRepository;
	@Autowired
	private LocalCacheConfig localCacheConfig;
	@Autowired
	private UpdateViewCountPostRepository updateViewCountPostRepository;
	@Autowired
	private AuthenticationUtil authenticationUtil;
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
			post.setSlug(p.getPostName());
			returnPostList.add(post);
		}

		return returnPostList;
	}

	@Override
	public ViewCountPost updateViewCount(int postId, HttpServletRequest httpServletRequest) {
		Optional<ViewCountPost> optViewCountPost = updateViewCountPostRepository.findByPostId(postId);
		if (optViewCountPost.isPresent()) {
			ViewCountPost viewCountPost = optViewCountPost.get();
			viewCountPost.setViewCount(viewCountPost.getViewCount() + 1);
			ViewCountPost updatedPost = updateViewCountPostRepository.saveAndFlush(viewCountPost);
			return updatedPost;
		}
		Post post = postNativeRepository.findNewestPostById(postId);
		if (post == null) {
			return null;
		}

		Integer userId = null;
		String token = authenticationUtil.extractTokenFromRequest(httpServletRequest);
		if (!ValidtionUtils.checkEmptyOrNull(token)) {
			User user = authenticationUtil.getUserFromToken(token);
			userId = user.getId();
		}
		ViewCountPost viewCountPost = ViewCountPost.builder()
				.postId(post.getId())
				.viewCount(1)
				.slug(post.getSlug())
				.userId(userId)
				.createdDate(new Timestamp(System.currentTimeMillis()))
				.build();

		return updateViewCountPostRepository.saveAndFlush(viewCountPost);
	}

	@Override
	public List<Post> findLastTopPost(int size) {
		List<Post> returnListPost = new ArrayList<>();
		List<ViewCountPost> viewCountPostList = updateViewCountPostRepository.findAllByOrderByViewCountDescUpdatedDateDesc();
		List<ViewCountPost> topPostList = viewCountPostList.stream().limit(size).collect(Collectors.toList());

		Map<Integer, String> imageMap = menuRepository.findAllImageRepresent();
		Map<Integer, String> descriptionMap = menuRepository.findAllDescription();

		for (ViewCountPost parent : topPostList) {
			List<Post> lastPostList = postRepository.findAllLastPostByParentPost(parent.getPostId());
			if (lastPostList.isEmpty()) {
				continue;
			}
			Post post = lastPostList.get(0);
			String image = "";
			if (imageMap.containsKey(parent.getPostId())) {
				image = imageMap.get(parent.getPostId());
			}
			String description = "";
			if (descriptionMap.containsKey(parent.getPostId())) {
				description = descriptionMap.get(parent.getPostId());
			}
			post.setMenuImage(image);
			post.setDescription(description);
			returnListPost.add(post);
		}
		return returnListPost;
	}

	@Override
	public List<Post> findLastTopPostOrderByMonth(int size, int orderBy) {
		List<Post> returnListPost = new ArrayList<>();
		List<ViewCountPost> viewCountPostList = updateViewCountPostRepository.findAllByOrderByViewCountDescUpdatedDateDesc();

		Map<Integer, String> imageMap = menuRepository.findAllImageRepresent();
		Map<Integer, String> descriptionMap = menuRepository.findAllDescription();
		Map<String, Post> mapViewPost = new HashMap<>();

		for (ViewCountPost parent : viewCountPostList) {
			List<Post> lastPostList = postRepository.findAllLastPostByParentPost(parent.getPostId());
			if (lastPostList.isEmpty()) {
				continue;
			}
			Post post = lastPostList.get(0);
			String keyPostDate = FormatUtil.formatPostDate(post.getPostModified());
			if (mapViewPost.containsKey(keyPostDate)) {
				continue;
			}
			String image = "";
			if (imageMap.containsKey(parent.getPostId())) {
				image = imageMap.get(parent.getPostId());
			}
			String description = "";
			if (descriptionMap.containsKey(parent.getPostId())) {
				description = descriptionMap.get(parent.getPostId());
			}
			post.setMenuImage(image);
			post.setDescription(description);
			if (!mapViewPost.containsKey(keyPostDate)) {
				mapViewPost.put(keyPostDate, post);
				returnListPost.add(post);
			}
		}
		return returnListPost;
	}

	@Override
	public List<Post> findAllPostByMenu(String menuCode) {
		List<Post> postList = postRepository.findAllPostByMenuCode(menuCode);
		return postList;
	}

	@Override
	public Post findById(int postId) {
		return postRepository.findPostById(postId);
	}
}
