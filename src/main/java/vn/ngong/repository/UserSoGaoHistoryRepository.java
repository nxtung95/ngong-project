package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ngong.dto.sogao.UserSoGaoHistoryDto;
import vn.ngong.entity.UserSoGaoHistory;

import java.util.List;

@Repository
public interface UserSoGaoHistoryRepository extends JpaRepository<UserSoGaoHistory, Integer> {
    @Query(
        value = "SELECT ug.sogao_code as soGaoCode, v.name as soGaoName, ug.purchase_date as purchaseDate, h.created_at as createdAt, h.used_number as usedNumber, h.remaining_number as remainingNumber " +
                " FROM wp_user_sogao_histories h" +
                " INNER JOIN wp_user_sogaos ug ON ug.id = h.user_sogao_id" +
                " INNER JOIN wp_customer_user u ON u.id = ug.user_id" +
                " INNER JOIN wp_product_variants v ON v.id = ug.product_id" +
                " WHERE u.id = :userId and v.status = 1 ORDER BY ug.purchase_date DESC ",
        nativeQuery = true)
    List<UserSoGaoHistoryDto> list(int userId);
}
