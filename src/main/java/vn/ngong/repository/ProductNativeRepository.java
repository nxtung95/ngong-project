package vn.ngong.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.ngong.dto.MenuDto;
import vn.ngong.entity.Product;
import vn.ngong.helper.FormatUtil;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterDetail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ProductNativeRepository {
    @Autowired
    private ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<ProductFilterDetail> findBestSeller(int limit) {
        try {
            Query query = entityManager.createNativeQuery("SELECT p.id, p.`name`, p.brand_name, p.origin, p.category_id, p.product_images, p.so_gao_flag," +
                    " CASE WHEN MIN(v.price) <> MAX(v.price) THEN CONCAT(MIN(v.price), ' - ', MAX(v.price))" +
                    " ELSE MAX(v.price) END price," +
                    " CASE WHEN MIN(v.sale_prices) <> MAX(v.sale_prices) THEN CONCAT(MIN(v.sale_prices), ' - ', MAX(v.sale_prices))" +
                    " ELSE MAX(v.sale_prices) END sale_prices," +
                    " CAST(MAX(v.sale_prices * 100 / v.price) AS INT) sale_rate" +
                    " FROM transactions T " +
                    "INNER JOIN orders O ON O.Id = T.order_id " +
                    "INNER JOIN detail_orders D ON O.Id = D.order_id " +
                    "INNER JOIN wp_products p ON p.`id` = D.product_id " +
                    "LEFT JOIN wp_product_variants v ON p.Id = v.product_id AND v.`status` = 1 " +
                    "WHERE T.`status` = 3 " +
                    "AND p.`status` = 1 " +
                    " AND v.`status` = 1" +
                    " GROUP BY p.id " +
                    "ORDER BY quantity DESC LIMIT " + limit);

            List<Object[]> objects = query.getResultList();
            List<ProductFilterDetail> products = new ArrayList<>();
            for (Object[] obj : objects) {
                products.add(ProductFilterDetail.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .name((String) obj[1])
                        .brandName((String) obj[2])
                        .origin((String) obj[3])
                        .categoryId(Integer.parseInt((obj[4]).toString()))
                        .image((String) obj[5])
                        .soGaoFlag(Boolean.parseBoolean((obj[6]).toString()) ? 1 : 0)
                        .price((String) obj[7])
                        .salePrice((String) obj[8])
                        .saleRate(Integer.parseInt((obj[9]).toString()))
                        .build());
            }

            return products;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public List<ProductFilterDetail> findNewestSale(int limit) {
        try {
            Query query = entityManager.createNativeQuery("SELECT p.id, p.`name`, p.brand_name, p.origin, p.category_id, p.product_images, p.so_gao_flag," +
                    " CASE WHEN MIN(v.price) <> MAX(v.price) THEN CONCAT(MIN(v.price), ' - ', MAX(v.price))" +
                    " ELSE MAX(v.price) END price," +
                    " CASE WHEN MIN(v.sale_prices) <> MAX(v.sale_prices) THEN CONCAT(MIN(v.sale_prices), ' - ', MAX(v.sale_prices))" +
                    " ELSE MAX(v.sale_prices) END sale_prices," +
                    " CAST(MAX(v.sale_prices * 100 / v.price) AS INT) sale_rate" +
                    " FROM wp_products p " +
                    "INNER JOIN wp_product_variants v ON p.Id = v.product_id AND v.`status` = 1 " +
                    "WHERE p.`status` = 1 AND v.sale_prices <> v.price" +
                    " GROUP BY p.id " +
                    "ORDER BY v.updated_at DESC LIMIT " + limit);

            List<Object[]> objects = query.getResultList();
            List<ProductFilterDetail> products = new ArrayList<>();
            for (Object[] obj : objects) {
                products.add(ProductFilterDetail.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .name((String) obj[1])
                        .brandName((String) obj[2])
                        .origin((String) obj[3])
                        .categoryId(Integer.parseInt((obj[4]).toString()))
                        .image((String) obj[5])
                        .soGaoFlag(Boolean.parseBoolean((obj[6]).toString()) ? 1 : 0)
                        .price((String) obj[7])
                        .salePrice((String) obj[8])
                        .saleRate(Integer.parseInt((obj[9]).toString()))
                        .build());
            }

            return products;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    public List<ProductFilterDetail> list(ProductFilterRequest filter) {
        try {
            String order = filter.getOrderType() == 0 ? " ORDER BY v.price" : " ORDER BY v.price DESC";
            String paging = " LIMIT " + filter.getPageSize() + " OFFSET " + ((filter.getPageIndex()) * filter.getPageSize());
            Query query = entityManager.createNativeQuery("SELECT p.id, p.`name`, p.brand_name, p.origin, p.category_id, p.product_images, p.so_gao_flag," +
                    " CASE WHEN MIN(v.price) <> MAX(v.price) THEN CONCAT(MIN(v.price), ' - ', MAX(v.price))" +
                    " ELSE MAX(v.price) END price," +
                    " CASE WHEN MIN(v.sale_prices) <> MAX(v.sale_prices) THEN CONCAT(MIN(v.sale_prices), ' - ', MAX(v.sale_prices))" +
                    " ELSE MAX(v.sale_prices) END sale_prices," +
                    " CAST(MAX(v.sale_prices * 100 / v.price) AS INT) sale_rate" +
                    " FROM wp_products p" +
                    " INNER JOIN wp_product_variants v ON p.id = v.product_id" +
                    " WHERE p.`status` = 1" +
                    " AND v.`status` = 1" +
                    " AND (:keySearch = '' OR p.`name` LIKE '%' + :keySearch + '%')" +
                    " AND (:categoryId = 0 OR p.category_id = :categoryId)" +
                    " AND (:brandName = '' OR p.brand_name = :brandName)" +
                    " AND ((:maxPrice = -1 AND v.price >= :minPrice) OR (v.price BETWEEN :minPrice AND :maxPrice))" +
                    " GROUP BY p.id" + order + paging);

            query.setParameter("keySearch", filter.getProductName());
            query.setParameter("categoryId", filter.getCategoryId());
            query.setParameter("brandName", filter.getBrandName());
            query.setParameter("minPrice", filter.getMinPrice());
            query.setParameter("maxPrice", filter.getMaxPrice());

            List<Object[]> objects = query.getResultList();
            List<ProductFilterDetail> products = new ArrayList<>();
            for (Object[] obj : objects) {
                products.add(ProductFilterDetail.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .name((String) obj[1])
                        .brandName((String) obj[2])
                        .origin((String) obj[3])
                        .categoryId(Integer.parseInt((obj[4]).toString()))
                        .image((String) obj[5])
                        .soGaoFlag(Boolean.parseBoolean((obj[6]).toString()) ? 1 : 0)
                        .price((String) obj[7])
                        .salePrice((String) obj[8])
                        .saleRate(Integer.parseInt((obj[9]).toString()))
                        .build());
            }

            return products;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

}
