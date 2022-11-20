package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ngong.entity.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserId(int userId);
    Cart findAllByUserIdAndProductVariantId(int userId, int ProductVariantId);
    Cart findById(int id);
}
