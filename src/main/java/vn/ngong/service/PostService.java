package vn.ngong.service;

import vn.ngong.entity.Post;

public interface PostService {
	Post findPostByMenu(String menuCode);
}
