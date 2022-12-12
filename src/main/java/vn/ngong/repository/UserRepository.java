package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByEmail(String email);

	Optional<User> findByEmailOrPhone(String email, String phone);

	Optional<User> findByPhone(String phone);

	Optional<User> findByPhoneAndActived(String phone, int actived);

}
