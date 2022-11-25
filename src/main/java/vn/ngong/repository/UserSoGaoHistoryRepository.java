package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ngong.dto.UserSoGaoHistoryDto;
import vn.ngong.entity.UserSoGaoHistory;

import java.util.List;

@Repository
public interface UserSoGaoHistoryRepository extends JpaRepository<UserSoGaoHistory, Integer> {
    @Query(
        value = "SELECT h.id, v.id soGaoId, u.id userId, v.name soGaoName, ug.sogao_code soGaoCode, ug.`status`, h.used_number usedNumber, h.remaining_number remainingNumber" +
                " FROM wp_user_sogao_histories h" +
                " INNER JOIN wp_user_sogaos ug ON ug.id = h.user_sogao_id" +
                " INNER JOIN wp_customer_user u ON u.id = ug.user_id" +
                " INNER JOIN wp_product_variants v ON v.id = ug.product_id" +
                " INNER JOIN (SELECT MAX(id) id, user_sogao_id FROM wp_user_sogao_histories GROUP BY user_sogao_id) tmp ON tmp.id = h.id AND tmp.user_sogao_id = ug.id" +
                " WHERE u.id = :userId",
        nativeQuery = true)
    List<UserSoGaoHistoryDto> list(int userId);
}
