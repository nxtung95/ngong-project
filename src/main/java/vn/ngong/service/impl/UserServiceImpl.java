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
import vn.ngong.helper.AuthenticationUtil;
import vn.ngong.helper.ValidtionUtils;
import vn.ngong.repository.UserRepository;
import vn.ngong.request.PaymentRequest;
import vn.ngong.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoginServiceImpl loginService;
	@Autowired
	private AuthenticationUtil authenticationUtil;

	@Override
	public User login(String username, String password) {
		try {
			log.info("Login with user: " + username);
			UserDetails userDetails = loginService.loadUserByUsername(username);
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
	public boolean checkExistByEmail(String mail, int userId) {
		User user = userRepository.findByEmail(mail).orElse(null);
		if (user == null) {
			return true;
		}
		if (user.getId() == userId) {
			return false;
		}
		return true;
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

	@Override
	public User makeUserForPayment(PaymentRequest rq) {
		String currentPhoneUser = authenticationUtil.getPhoneLoginName();
		log.info("currentPhoneUser: ", currentPhoneUser);
		User user;
		// User đặt hàng chưa đăng nhập
		if (ValidtionUtils.checkEmptyOrNull(currentPhoneUser) || "anonymousUser".equalsIgnoreCase(currentPhoneUser)) {
			// Kiểm tra có trong hệ thống không?
			Optional<User> optionalUser = findByPhone(rq.getCustomer().getCusPhone());
			if (optionalUser == null) {
				log.info("Lỗi user null");
				return null;
			}

			// Đã có trong hệ thống
			if (optionalUser.isPresent()) {
				user = optionalUser.get();
			} else {
				// Chưa có trong hệ thống
				String passwordDefault = authenticationUtil.makeDefaultPassword();
				log.info("passwordDefault: " + passwordDefault);
				User newUser = User.builder()
						.name(rq.getCustomer().getCusName())
						.phone(rq.getCustomer().getCusPhone())
						.password(passwordEncoder.encode(passwordDefault))
						.passwordPlainText(passwordDefault)
						.email(rq.getCustomer().getCusEmail())
						.address(rq.getCustomer().getCusWard() + "," + rq.getCustomer().getCusDistrict() + "," + rq.getCustomer().getCusCity())
						.actived(0)
						.build();
				user = add(newUser);
			}
		} else {
			// Kiểm tra có trong hệ thống không?
			Optional<User> optionalUser = findByPhone(rq.getCustomer().getCusPhone());
			if (optionalUser == null) {
				log.info("Lỗi user null");
				return null;
			}
			user = optionalUser.get();
		}
		return user;
	}

	@Override
	public User findById(Integer userId) {
		try {
			User user = userRepository.findById(userId).orElse(null);
			if (user == null) {
				return null;
			}
			return user;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
