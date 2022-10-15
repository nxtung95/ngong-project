package vn.ngong.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JWTUserDetailsService implements UserDetailsService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	private List<User> userList = new ArrayList<>();

	@PostConstruct
	public void init() {
		userList.add(new User("user_ngong@gmail.com",
				"$2a$10$SWjR2.lA/gMZzrGdMpkb4.L3VRzszgFxLmo43PVt4CDXQv.IjQhXi", new ArrayList<>()));
	}

	public void add(vn.ngong.entity.User user) {
		userList.add(new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), new ArrayList<>()));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optUser = userList.stream().filter(u -> username.equals(u.getUsername())).findFirst();
		if (!optUser.isPresent()) {
			return null;
		}
		User user = optUser.get();
		return user;
	}
}
