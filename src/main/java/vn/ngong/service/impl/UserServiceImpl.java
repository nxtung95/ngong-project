package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ngong.service.JWTUserDetailsService;
import vn.ngong.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
	private JWTUserDetailsService jwtUserDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private List<vn.ngong.entity.User> userList = new ArrayList<>();

	@Override
	public UserDetails login(String username, String password) {
		try {
			log.info("Login with user: " + username);
			UserDetails user = jwtUserDetailsService.loadUserByUsername(username);
			if (!passwordEncoder.matches(password, user.getPassword())) {
				return null;
			}
			return user;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public boolean register(vn.ngong.entity.User user) {
		userList.add(user);
		jwtUserDetailsService.add(user);
		return true;
	}
}
