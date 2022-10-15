package vn.ngong.service;

import org.springframework.security.core.userdetails.UserDetails;
import vn.ngong.entity.User;

public interface UserService {
	UserDetails login(String username, String password);

	boolean register(vn.ngong.entity.User user);

	boolean checkExist(User user);
}
