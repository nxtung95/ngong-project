package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.dto.ProductVariantDto;
import vn.ngong.entity.ProductVariant;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> findAllByProductIdAndStatus(int productId, int status);

    List<ProductVariant> findAllByCodeInAndStatus(List<String> productCode, int status);

    Optional<ProductVariant> findByCodeAndStatus(String productCode, int status);
}
