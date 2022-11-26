package vn.ngong.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.ngong.dto.MenuDto;
import vn.ngong.dto.ProductVariantDto;
import vn.ngong.entity.Product;
import vn.ngong.helper.FormatUtil;
import vn.ngong.kiotviet.obj.Attribute;
import vn.ngong.request.ProductFilterRequest;
import vn.ngong.response.ProductFilterDetail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
public class ProductNativeRepository {
    @Autowired
    private ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private Gson gson;

    public List<ProductFilterDetail> findBestSeller(int limit) {
        try {
            Query query = entityManager.createNativeQuery("SELECT p.id, p.`name`, p.brand_name, p.origin, p.category_id, p.product_images, p.so_gao_flag," +
                    " CASE WHEN MIN(v.price) <> MAX(v.price) THEN CONCAT(MIN(v.price), ' - ', MAX(v.price))" +
                    " ELSE MAX(v.price) END price," +
                    " CASE WHEN MIN(v.sale_prices) <> MAX(v.sale_prices) THEN CONCAT(MIN(v.sale_prices), ' - ', MAX(v.sale_prices))" +
                    " ELSE MAX(v.sale_prices) END sale_prices," +
                    " CAST(MAX(v.sale_prices * 100 / v.price) AS INT) sale_rate," +
                    " v2.id variant_id, v2.price variant_price, v2.sale_prices variant_sale_prices, v2.weight, v2.variants_detail," +
                    " SUM(IFNULL(D.quantity, 0))" +
//                    " FROM transactions T " +
//                    "INNER JOIN orders O ON O.Id = T.order_id " +
                    " FROM detail_orders D" +
                    " LEFT JOIN wp_product_variants v ON D.product_code = v.code" +
                    " INNER JOIN wp_products p ON p.`id` = v.product_id " +
                    " INNER JOIN (SELECT product_id, MIN(id) id FROM wp_product_variants GROUP BY product_id) tmp ON p.id = tmp.product_id" +
                    " INNER JOIN wp_product_variants v2 ON p.id = v2.product_id AND tmp.id = v2.id" +
                    //" WHERE T.`status` = 3 " +
                    " WHERE p.`status` = 1 " +
                    " AND v.`status` = 1" +
                    " GROUP BY p.id " +
                    "ORDER BY SUM(IFNULL(D.quantity, 0)) DESC LIMIT " + limit);

            List<Object[]> objects = query.getResultList();
            List<ProductFilterDetail> products = new ArrayList<>();
            for (Object[] obj : objects) {
                ProductVariantDto variant = ProductVariantDto
                        .builder()
                        .id(Integer.parseInt((obj[10]).toString()))
                        .price(Integer.parseInt((obj[11]).toString()))
                        .salePrice(Integer.parseInt((obj[12]).toString()))
                        .weight(Double.parseDouble((obj[13]).toString()))
                        .variantDetail(gson.fromJson((obj[14]).toString() == null ? "" : (obj[14]).toString(), Object.class))
                        .build();

                products.add(ProductFilterDetail.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .name((String) obj[1])
                        .brandName((String) obj[2])
                        .origin((String) obj[3])
                        .categoryId(Integer.parseInt((obj[4]).toString()))
                        .image(gson.fromJson((String) obj[5] == null ? "" : (String) obj[5], new TypeToken<List<String>>(){}.getType()))
                        .soGaoFlag(Boolean.parseBoolean((obj[6]).toString()) ? 1 : 0)
                        .price((String) obj[7])
                        .salePrice((String) obj[8])
                        .saleRate(Integer.parseInt((obj[9]).toString()))
                        .selledNumber(Integer.parseInt((obj[15]).toString()))
                        .productVariant(variant)
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
                    " CAST(MAX(v.sale_prices * 100 / v.price) AS INT) sale_rate," +
                    " v2.id variant_id, v2.price variant_price, v2.sale_prices variant_sale_prices, v2.weight, v2.variants_detail," +
                    " SUM(IFNULL(o.quantity, 0))" +
                    " FROM wp_products p " +
                    "INNER JOIN wp_product_variants v ON p.Id = v.product_id AND v.`status` = 1 " +
                    " INNER JOIN (SELECT product_id, MIN(id) id FROM wp_product_variants GROUP BY product_id) tmp ON p.id = tmp.product_id" +
                    " INNER JOIN wp_product_variants v2 ON p.id = v2.product_id AND tmp.id = v2.id" +
                    " LEFT JOIN detail_orders o ON o.product_code = v.code" +
                    " WHERE p.`status` = 1 AND v.sale_prices <> v.price" +
                    " GROUP BY p.id " +
                    "ORDER BY v.updated_at DESC LIMIT " + limit);

            List<Object[]> objects = query.getResultList();
            List<ProductFilterDetail> products = new ArrayList<>();
            for (Object[] obj : objects) {
                ProductVariantDto variant = ProductVariantDto
                        .builder()
                        .id(Integer.parseInt((obj[10]).toString()))
                        .price(Integer.parseInt((obj[11]).toString()))
                        .salePrice(Integer.parseInt((obj[12]).toString()))
                        .weight(Double.parseDouble((obj[13]).toString()))
                        .variantDetail(gson.fromJson((obj[14]).toString() == null ? "" : (obj[14]).toString(), Object.class))
                        .build();

                products.add(ProductFilterDetail.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .name((String) obj[1])
                        .brandName((String) obj[2])
                        .origin((String) obj[3])
                        .categoryId(Integer.parseInt((obj[4]).toString()))
                        .image(gson.fromJson((String) obj[5] == null ? "" : (String) obj[5], new TypeToken<List<String>>(){}.getType()))
                        .soGaoFlag(Boolean.parseBoolean((obj[6]).toString()) ? 1 : 0)
                        .price((String) obj[7])
                        .salePrice((String) obj[8])
                        .saleRate(Integer.parseInt((obj[9]).toString()))
                        .selledNumber(Integer.parseInt((obj[15]).toString()))
                        .productVariant(variant)
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
                    " CAST(MAX(v.sale_prices * 100 / v.price) AS INT) sale_rate," +
                    " v2.id variant_id, v2.price variant_price, v2.sale_prices variant_sale_prices, v2.weight, v2.variants_detail," +
                    " SUM(IFNULL(o.quantity, 0))" +
                    " FROM wp_products p" +
                    " INNER JOIN wp_product_variants v ON p.id = v.product_id" +
                    " INNER JOIN (SELECT product_id, MIN(id) id FROM wp_product_variants GROUP BY product_id) tmp ON p.id = tmp.product_id" +
                    " INNER JOIN wp_product_variants v2 ON p.id = v2.product_id AND tmp.id = v2.id" +
                    " LEFT JOIN detail_orders o ON o.product_code = v.code" +
                    " WHERE p.`status` = 1" +
                    " AND v.`status` = 1" +
                    " AND (:keySearch = '' OR p.`name` LIKE '%' + :keySearch + '%')" +
                    " AND (:categoryId = 0 OR p.category_id = :categoryId)" +
                    " AND (:brandName = '' OR p.brand_name = :brandName)" +
                    " AND ((:maxPrice = -1 AND v.price >= :minPrice) OR (v.price BETWEEN :minPrice AND :maxPrice))" +
                    " GROUP BY p.id" + order + paging);
                    //" ORDER BY p.category_id, p.id");

            query.setParameter("keySearch", filter.getProductName());
            query.setParameter("categoryId", filter.getCategoryId());
            query.setParameter("brandName", filter.getBrandName());
            query.setParameter("minPrice", filter.getMinPrice());
            query.setParameter("maxPrice", filter.getMaxPrice());

            List<Object[]> objects = query.getResultList();
            List<ProductFilterDetail> products = new ArrayList<>();
            for (Object[] obj : objects) {
                ProductVariantDto variant = ProductVariantDto
                        .builder()
                        .id(Integer.parseInt((obj[10]).toString()))
                        .price(Integer.parseInt((obj[11]).toString()))
                        .salePrice(Integer.parseInt((obj[12]).toString()))
                        .weight(Double.parseDouble((obj[13]).toString()))
                        .variantDetail(gson.fromJson((obj[14]).toString() == null ? "" : (obj[14]).toString(), Object.class))
                        .build();

                products.add(ProductFilterDetail.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .name((String) obj[1])
                        .brandName((String) obj[2])
                        .origin((String) obj[3])
                        .categoryId(Integer.parseInt((obj[4]).toString()))
                        .image(gson.fromJson(obj[5] == null ? "" : (String) obj[5], new TypeToken<List<String>>(){}.getType()))
                        .soGaoFlag(Boolean.parseBoolean((obj[6]).toString()) ? 1 : 0)
                        .price((String) obj[7])
                        .salePrice((String) obj[8])
                        .saleRate(Integer.parseInt((obj[9]).toString()))
                        .productVariant(variant)
                        .selledNumber(Integer.parseInt((obj[15]).toString()))
                        .build());
            }

            return products;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

}
