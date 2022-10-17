package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ngong.entity.User;
import vn.ngong.repository.UserRepository;
import vn.ngong.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
//	@Autowired
//	private JWTUserDetailsService jwtUserDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

//	private List<vn.ngong.entity.User> userList = new ArrayList<>();

	@Override
	public User login(String username, String password) {
		try {
			log.info("Login with user: " + username);
			User user = userRepository.findByEmail(username).orElse(null);
			if (user == null) {
				return null;
			}
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
		try {
			userRepository.saveAndFlush(user);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public boolean checkExist(User user) {
		return userRepository.findByEmail(user.getEmail()).isPresent();
	}

	@Override
	public boolean update(User user) {
		try {
			User userByEmail = userRepository.findByEmail(user.getEmail()).orElse(null);
			if (userByEmail == null) {
				return false;
			}
			userRepository.saveAndFlush(userByEmail);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}
}
