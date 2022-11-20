package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.UserSoGao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSoGaoRepository extends JpaRepository<UserSoGao, Integer> {
	List<UserSoGao> findAllByUserIdAndStatusAndExpireDateAfterOrderByExpireDateAsc(int userId, int status, Timestamp expireDate);

	Optional<UserSoGao> findTopByOrderByIdDesc();
}
