package vn.ngong.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Post;
import vn.ngong.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	Optional<Product> findByCodeAndStatus(String code, int status);
	List<Product> findAllByNameLike(String name);

	List<Product> findAllByNameLikeAndPriceIsBetweenAndStatusOrderByPrice(String name, long priceMin, long priceMax, int status, Pageable pageable);
	List<Product> findAllByNameLikeAndPriceIsBetweenAndStatusOrderByPriceDesc(String name, long priceMin, long priceMax, int status, Pageable pageable);
	List<Product> findAllByNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPrice(String name, String brand, long priceMin, long priceMax, int status, Pageable pageable);
	List<Product> findAllByNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPriceDesc(String name, String brand, long priceMin, long priceMax, int status, Pageable pageable);

	List<Product> findAllByCategoryIdAndNameLikeAndPriceIsBetweenAndStatusOrderByPrice(int categoryId, String name, long priceMin, long priceMax, int status, Pageable pageable);
	List<Product> findAllByCategoryIdAndNameLikeAndPriceIsBetweenAndStatusOrderByPriceDesc(int categoryId, String name, long priceMin, long priceMax, int status, Pageable pageable);
	List<Product> findAllByCategoryIdAndNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPrice(int categoryId, String name, String brand, long priceMin, long priceMax, int status, Pageable pageable);
	List<Product> findAllByCategoryIdAndNameLikeAndBrandNameAndPriceIsBetweenAndStatusOrderByPriceDesc(int categoryId, String name, String brand, long priceMin, long priceMax, int status, Pageable pageable);
}
