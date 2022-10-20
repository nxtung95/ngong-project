package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Product;
import vn.ngong.entity.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    Sale findFirstByProductIdAndStatusOrderByUpdatedAtDesc(int productId, int status);
}
