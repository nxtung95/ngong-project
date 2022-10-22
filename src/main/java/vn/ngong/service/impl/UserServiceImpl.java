package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ngong.entity.User;
import vn.ngong.helper.ValidtionUtils;
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
		User user = optUser.get();

		SimpleGrantedAuthority role = new SimpleGrantedAuthority("user");
		Collection<SimpleGrantedAuthority> roleList = new ArrayList<>();
		roleList.add(role);
		org.springframework.security.core.userdetails.User userDetail;
		if (!ValidtionUtils.checkEmptyOrNull(optUser.get().getPasswordPlainText())) {
			// Login qua tạo tài khoản khi thanh toán/ admin
			String passwordPlainText = user.getPasswordPlainText();
			String hashPass = passwordEncoder.encode(passwordPlainText);
			user.setPassword(hashPass);
			user.setPasswordPlainText("");
			userRepository.saveAndFlush(user);
		}
		try {
			// Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
//			HttpServletRequest request =
//					((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//							.getRequest();
//			UsernamePasswordAuthenticationToken authenticationToken =
//					new UsernamePasswordAuthenticationToken(user.getPhone(), user.getPassword(), roleList);
//			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////			Authentication authentication = authenticationManager.authenticate(authenticationToken);
//			// After setting the Authentication in the context, we specify
//			// that the current user is authenticated. So it passes the
//			// Spring Security Configurations successfully.
//			log.info("Principal: " + authenticationToken.getPrincipal());
//			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			userDetail = new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPassword(), roleList);
			return userDetail;
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			log.error("Failure in autoLogin", e);
		}
		return null;
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

	@Override
	public Optional<User> findByPhone(String phone) {
		try {
			return userRepository.findByPhone(phone);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public User add(User user) {
		try {
			return userRepository.saveAndFlush(user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
