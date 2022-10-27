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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class LoginServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optUser = userRepository.findByPhoneAndActived(username, 1);
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
}
