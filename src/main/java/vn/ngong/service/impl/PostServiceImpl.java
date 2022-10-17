package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Menu;
import vn.ngong.entity.Post;
import vn.ngong.repository.PostRepository;
import vn.ngong.service.PostService;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepository postRepository;

	@Override
	public Post findPostByMenu(String menuCode) {
		List<Post> postList = postRepository.findPostByMenuCode(menuCode);
		if (postList.isEmpty()) {
			return null;
		}
		postList.sort(Comparator.comparing(Post::getPostDate).reversed());
		List<Post> lastPostList = postRepository.findAllLastPostByParentPost(postList.get(0).getId());
		lastPostList.sort(Comparator.comparing(Post::getPostDate).reversed());
		Post post = lastPostList.get(0);
		post.setPostContent(post.getPostContent().replace("\n", ""));
		return post;
	}
}
