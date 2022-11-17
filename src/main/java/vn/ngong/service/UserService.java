package vn.ngong.service;

import org.springframework.security.core.userdetails.UserDetails;
import vn.ngong.entity.User;
import vn.ngong.request.PaymentRequest;

import java.util.Optional;

public interface UserService {
	User login(String username, String password);

	boolean register(vn.ngong.entity.User user);

	boolean checkExistByPhoneOrEmail(User user);

	boolean checkExistByPhone(String phone);

	boolean checkExistByEmail(String mail);

	boolean update(User user);

	Optional<User> findByPhone(String phone);

	User add(User user);

	User makeUserForPayment(PaymentRequest rq);

	User findById(Integer userId);
}
