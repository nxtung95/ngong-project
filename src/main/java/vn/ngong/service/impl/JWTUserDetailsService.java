package vn.ngong.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ngong.enums.UserTypeEnum;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class JWTUserDetailsService implements UserDetailsService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	private List<User> userList = new ArrayList<>();

	@PostConstruct
	public void init() {
		GrantedAuthority userAuthority = new SimpleGrantedAuthority(String.valueOf(UserTypeEnum.USER.label()));
		Set<GrantedAuthority> userGrantedAuthorities = new HashSet<>();
		userGrantedAuthorities.add(userAuthority);
		userList.add(new User("user_ngong@gmail.com",
				"$2a$10$SWjR2.lA/gMZzrGdMpkb4.L3VRzszgFxLmo43PVt4CDXQv.IjQhXi", userGrantedAuthorities));

		GrantedAuthority adminAuthority = new SimpleGrantedAuthority(String.valueOf(UserTypeEnum.ADMIN.label()));
		Set<GrantedAuthority> adminGrantedAuthorities = new HashSet<>();
		adminGrantedAuthorities.add(adminAuthority);
		adminGrantedAuthorities.add(userAuthority);
		userList.add(new User("admin_ngong@gmail.com",
				"$2a$10$SWjR2.lA/gMZzrGdMpkb4.L3VRzszgFxLmo43PVt4CDXQv.IjQhXi", adminGrantedAuthorities));
	}

	public void add(vn.ngong.entity.User user) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		if (UserTypeEnum.USER.label().equals(user.getType())) {
			GrantedAuthority userAuthority = new SimpleGrantedAuthority(String.valueOf(UserTypeEnum.USER.label()));
			grantedAuthorities.add(userAuthority);
		} else {
			GrantedAuthority adminAuthority = new SimpleGrantedAuthority(String.valueOf(UserTypeEnum.ADMIN.label()));
			GrantedAuthority userAuthority = new SimpleGrantedAuthority(String.valueOf(UserTypeEnum.USER.label()));
			grantedAuthorities.add(adminAuthority);
			grantedAuthorities.add(userAuthority);
		}
		userList.add(new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), grantedAuthorities));
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
