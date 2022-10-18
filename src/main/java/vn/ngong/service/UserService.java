package vn.ngong.service;

import org.springframework.security.core.userdetails.UserDetails;
import vn.ngong.entity.User;

public interface UserService {
	User login(String username, String password);

	boolean register(vn.ngong.entity.User user);

	boolean checkExistByPhoneOrEmail(User user);

	boolean checkExistByPhone(String phone);

	boolean checkExistByEmail(String mail);

	boolean update(User user);
}
