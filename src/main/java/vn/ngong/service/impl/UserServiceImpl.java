package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ngong.entity.User;
import vn.ngong.repository.UserRepository;
import vn.ngong.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optUser = userRepository.findByPhone(username);
		if (!optUser.isPresent()) {
			return null;
		}
		SimpleGrantedAuthority role = new SimpleGrantedAuthority("user");
		Collection<SimpleGrantedAuthority> roleList = new ArrayList<>();
		roleList.add(role);
		org.springframework.security.core.userdetails.User userDetail = new
				org.springframework.security.core.userdetails.User(optUser.get().getPhone(), optUser.get().getPassword(), roleList);
		return userDetail;
	}

	@Override
	public User login(String username, String password) {
		try {
			log.info("Login with user: " + username);
			UserDetails userDetails = loadUserByUsername(username);
			if (userDetails == null) {
				return null;
			}
			if (!passwordEncoder.matches(password, userDetails.getPassword())) {
				return null;
			}
			return userRepository.findByPhone(username).get();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public boolean register(vn.ngong.entity.User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.saveAndFlush(user);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public boolean checkExistByPhoneOrEmail(User user) {
		return userRepository.findByEmailOrPhone(user.getEmail(), user.getPhone()).isPresent();
	}

	@Override
	public boolean checkExistByPhone(String phone) {
		return userRepository.findByPhone(phone).isPresent();
	}

	@Override
	public boolean checkExistByEmail(String mail) {
		return userRepository.findByEmail(mail).isPresent();
	}

	@Override
	public boolean update(User user) {
		try {
			User userByPhone = userRepository.findByPhone(user.getPhone()).orElse(null);
			if (userByPhone == null) {
				return false;
			}
			userRepository.saveAndFlush(userByPhone);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}
}
