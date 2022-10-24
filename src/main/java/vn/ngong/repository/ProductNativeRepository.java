package vn.ngong.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.ngong.dto.MenuDto;
import vn.ngong.entity.Product;
import vn.ngong.helper.FormatUtil;
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
            Query query = entityManager.createNativeQuery("SELECT P.id, P.name, P.code, P.price, P.product_images AS image, IFNULL(S.rate * 100, 0) AS saleRate, IFNULL(S.`name`, '') AS saleName, IFNULL(S.sale_price, 0) AS salePrice, IFNULL(S.start_time, '') AS startTime, IFNULL(S.end_time, '') AS endTime, SUM(D.quantity) quantity " +
                    "FROM transactions T " +
                    "INNER JOIN orders O ON O.Id = T.order_id " +
                    "INNER JOIN detail_orders D ON O.Id = D.order_id " +
                    "INNER JOIN wp_products P ON P.`code` = D.product_code " +
                    "LEFT JOIN product_sales S ON P.Id = S.product_id AND S.`status` = 1 " +
                    "WHERE T.`status` = 3 " +
                    "AND P.`status` = 1 " +
                    "GROUP BY P.code " +
                    "ORDER BY quantity DESC LIMIT " + limit);

            List<Object[]> objects = query.getResultList();
            List<ProductFilterDetail> products = new ArrayList<>();
            for (Object[] obj : objects) {
                products.add(ProductFilterDetail.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .name((String) obj[1])
                        .code((String) obj[2])
                        .price(Long.parseLong(obj[3].toString()))
                        .image((String) obj[4])
                        .saleRate((int) ((double)obj[5]))
                        .saleName((String) obj[6])
                        .salePrice(Long.parseLong(obj[7].toString()))
                        .saleStartTime(FormatUtil.stringToTimestamp(obj[8].toString()))
                        .saleEndTime(FormatUtil.stringToTimestamp(obj[9].toString()))
                        .build());

            }

            return products;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

}
