package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.UserSoGao;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserSoGaoRepository extends JpaRepository<UserSoGao, Integer> {
	List<UserSoGao> findAllByUserIdAndAndIsActiveAndExpireDateAfter(int userId, int isActive, Timestamp expireDate);
}
