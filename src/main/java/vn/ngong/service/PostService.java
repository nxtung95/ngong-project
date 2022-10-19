package vn.ngong.service;

import vn.ngong.entity.Post;

import java.util.List;

public interface PostService {
	List<Post> findPostByMenu(String menuCode, int limit, int offset);
}
