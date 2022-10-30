package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.UserSoGaoHistory;

@Repository
public interface UserSoGaoHistoryRepository extends JpaRepository<UserSoGaoHistory, Integer> {
}
