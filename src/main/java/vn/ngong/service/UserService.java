package vn.ngong.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
	UserDetails login(String username, String password);

	boolean register(vn.ngong.entity.User user);
}
